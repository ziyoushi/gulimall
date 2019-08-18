package com.atguigu.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.cart.feign.SkuCouponReductionFeignService;
import com.atguigu.gulimall.cart.feign.SkuFeignService;
import com.atguigu.gulimall.cart.service.CartService;
import com.atguigu.gulimall.cart.vo.CartItemVo;
import com.atguigu.gulimall.cart.vo.CartVo;
import com.atguigu.gulimall.cart.vo.SkuCouponVo;
import com.atguigu.gulimall.cart.vo.SkuFullReductionVo;
import com.atguigu.gulimall.commons.bean.Constant;
import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuInfoVo;
import com.atguigu.gulimall.commons.utils.GuliJwtUtils;
import lombok.Data;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @create 2019-08-16 16:15
 */
@Service
public class CartServiceImpl  implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    SkuFeignService skuFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    SkuCouponReductionFeignService couponReductionFeignService;

    @Override
    public CartVo getCart(String userKey, String authorization) throws ExecutionException, InterruptedException {

        CartVo cartVo = new CartVo();

        CartKey cartKey = getKey(userKey,authorization);

        if (cartKey.isLogin()){
            //如果登录了 需要合并购物车
            mergeCart(userKey,Long.parseLong(cartKey.getKey()));

        }else {
            //如果没有登录， 需要给前端返回 userKey
            cartVo.setUserKey(cartKey.getKey());
        }

        //获取购物项返回
        List<CartItemVo> itemVos = getCartItems(cartKey.getKey());
        cartVo.setItems(itemVos);

        return cartVo;
    }

    //合并购物车 将临时购物车中的数据取出后 转换成购物项
    // 存入到登录后的购物车中 并将临时购物车中的数据删除
    private void mergeCart(String userKey, Long userId) throws ExecutionException, InterruptedException {
        RMap<String, String> map = redissonClient.getMap(Constant.CART_PREFIX + userKey);
        if (map != null && map.size() >0){

            Collection<String> values = map.values();
            for (String value : values) {
                CartItemVo vo = JSON.parseObject(value, CartItemVo.class);
                //将临时购物车解析出来的购物项 添加到在线购物车中
                addCartItemVo(vo.getSkuId(),vo.getNum(),userId.toString());

            }

        }

        //合并完成后 将临时购物车删除
        redisTemplate.delete(Constant.CART_PREFIX+userKey);

    }

    //抽取添加商品到购物车
    private CartItemVo addCartItemVo(Long skuId, Integer num, String cartKey) throws ExecutionException, InterruptedException {

        CartItemVo vo = null;

        //查询购物车
        RMap<String, String> cart = redissonClient.getMap(Constant.CART_PREFIX + cartKey);
        String item = cart.get(skuId.toString());//查看购物车中是否有skuId这个商品
        //添加购物车之前先确定购物车中有没有这个商品，如果有就数量+1 如果没有新增
        if (!StringUtils.isEmpty(item)){
            CartItemVo itemVo = JSON.parseObject(item, CartItemVo.class);
            itemVo.setNum(itemVo.getNum()+num);
            //将这个sku商品存入到购物车中
            cart.put(skuId.toString(),JSON.toJSONString(itemVo));
            vo = itemVo;
        }else {

            CartItemVo itemVo = new CartItemVo();

            //封装sku基本信息
            CompletableFuture<Void> skuInfoAsync = CompletableFuture.runAsync(() -> {
                //远程调用查询sku的详情 异步调用
                //说明购物车中没有 要添加的商品 需要远程调用pms查询
                Resp<SkuInfoVo> skuInfoForCart = skuFeignService.getSkuInfoForCart(skuId);
                SkuInfoVo data = skuInfoForCart.getData();
                //拷贝数据
                BeanUtils.copyProperties(data, itemVo);
                itemVo.setNum(num);
            }, executor);

            //todo 查询优惠券 couponReductionFeignService
            //itemVo.setCouponVos();
            CompletableFuture<Void> couponAsync = CompletableFuture.runAsync(() -> {
                Resp<List<SkuCouponVo>> coupons = couponReductionFeignService.getCoupons(skuId);

                List<SkuCouponVo> data = coupons.getData();

                List<SkuCouponVo> skuCouponVos = new ArrayList<>();
                if (data != null && data.size() >0){
                    for (SkuCouponVo couponVo : data) {
                        SkuCouponVo skuCouponVo = new SkuCouponVo();
                        BeanUtils.copyProperties(couponVo,skuCouponVo);
                        skuCouponVos.add(skuCouponVo);
                    }
                    itemVo.setCouponVos(skuCouponVos);
                }
            }, executor);

            //todo 查询满减
            //itemVo.setReductionVos();
            CompletableFuture<Void> reductionAsync = CompletableFuture.runAsync(() -> {
                Resp<List<SkuFullReductionVo>> redutions = couponReductionFeignService.getRedutions(skuId);

                List<SkuFullReductionVo> data = redutions.getData();
                if (data != null && data.size() >0){
                    itemVo.setReductionVos(data);
                }

            }, executor);

            //等所有的查询出来再阻塞
            CompletableFuture.allOf(skuInfoAsync,couponAsync,reductionAsync).get();

            cart.put(skuId.toString(),JSON.toJSONString(itemVo));

            vo = itemVo;

        }

        return vo;
    }

    //根据key获取购物项
    private List<CartItemVo> getCartItems(String key) {
        List<CartItemVo> vos = new ArrayList<>();
        //不论是否登录 只要拼接key都可以从redis中查出购物项
        RMap<String, String> map = redissonClient.getMap(Constant.CART_PREFIX + key);
        Collection<String> values = map.values();
        if (values != null && values.size() > 0){
            for (String value : values) {
                CartItemVo cartItemVo = JSON.parseObject(value, CartItemVo.class);
                vos.add(cartItemVo);
            }
        }

        return vos;
    }

    @Override
    public CartVo addToCart(Long skuId, Integer num, String userKey, String authorization) throws ExecutionException, InterruptedException {

        CartKey cartKey = getKey(userKey, authorization);

        String key = cartKey.getKey();
        CartItemVo cartItemVo = addCartItemVo(skuId,num,cartKey.getKey());

        CartVo cartVo = new CartVo();
        //如果没登录带上useKey
        if (!cartKey.isLogin()){
            cartVo.setUserKey(key);
        }
        cartVo.setItems(Arrays.asList(cartItemVo));

        //如果没登录 一个月过期自动续期
        if (!cartKey.isLogin()){
            redisTemplate.expire(Constant.CART_PREFIX+key,Constant.CART_TIMEOUT, TimeUnit.MINUTES);
        }
        return cartVo;
    }

    @Override
    public CartVo updateCart(Long skuId, Integer num, String userKey, String authorization) {

        //先获得key 判断到底是登录了 还是没有登录
        CartKey cartKey = getKey(userKey, authorization);

        //根据key查看购物车 默认逻辑 能修改购物车 说明肯定有商品
        RMap<String, String> cart = redissonClient.getMap(Constant.CART_PREFIX + cartKey.getKey());

        String item = cart.get(skuId.toString());

        CartItemVo itemVo = JSON.parseObject(item, CartItemVo.class);
        //根据skuId查出购物项 修改数量
        itemVo.setNum(num);
        //将设置好的购物项存到redis中 覆盖(更新)
        cart.put(skuId.toString(),JSON.toJSONString(itemVo));

        //获取所有的购物项
        List<CartItemVo> cartItems = getCartItems(cartKey.getKey());

        //再封装购物车
        CartVo cartVo = new CartVo();
        cartVo.setItems(cartItems);

        return cartVo;
    }

    //就是修改的变相 将选中的状态改变
    @Override
    public CartVo checkCart(Long[] skuIds, Integer status, String userKey, String authorization) {

        //先获取key 登录还是临时
        CartKey cartKey = getKey(userKey, authorization);
        String key = cartKey.getKey();
        //从redis中查出购物车
        RMap<String, String> cart = redissonClient.getMap(Constant.CART_PREFIX + key);
        if (skuIds != null && skuIds.length >0){
            //说明真的有skuId传递过来

            for (Long skuId : skuIds) {
                String item = cart.get(skuId.toString());
                CartItemVo itemVo = JSON.parseObject(item, CartItemVo.class);
                itemVo.setCheck(status == 0?false:true);
                //更新购物车
                cart.put(skuId.toString(),JSON.toJSONString(itemVo));
            }

        }
        List<CartItemVo> cartItems = getCartItems(key);
        CartVo cartVo = new CartVo();
        cartVo.setItems(cartItems);

        return cartVo;
    }

    //抽取getKey 来判断是否登录
    private CartKey getKey(String userKey, String authorization) {

        CartKey cartKey = new CartKey();
        String key = "";
        //
        if (!StringUtils.isEmpty(authorization)){
            //authorization 说明已经登录
            //使用jwt工具类对authorization进行解密
            Map<String, Object> jwtBody = GuliJwtUtils.getJwtBody(authorization);
            Long id = Long.parseLong(jwtBody.get("id").toString());
            key  = id +"";
            cartKey.setKey(key);
            cartKey.setLogin(true);
            if (!StringUtils.isEmpty(userKey)){
                //说明 可能存在需要合并的购物车数据
                cartKey.setMerge(true);
            }
        }else {
            //没有登录
            //如果第一次就有userkey
            if (!StringUtils.isEmpty(userKey)){
                key = userKey;
                cartKey.setLogin(false);
                cartKey.setMerge(false);
            }else {
                //第一次就没有userkey就生成一个给前端
                key = UUID.randomUUID().toString().replace("-", "");
                cartKey.setTemp(true);
                cartKey.setLogin(false);
                cartKey.setMerge(false);

            }


        }
        cartKey.setKey(key);
        return cartKey;

    }

}

@Data
class CartKey {
    private String key;
    //是否登录
    private boolean login;
    //是否是临时
    private boolean temp;
    //是否需要合并
    private boolean merge;

}
