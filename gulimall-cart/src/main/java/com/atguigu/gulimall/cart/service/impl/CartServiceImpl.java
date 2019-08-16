package com.atguigu.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.cart.feign.SkuFeignService;
import com.atguigu.gulimall.cart.service.CartService;
import com.atguigu.gulimall.cart.vo.CartItemVo;
import com.atguigu.gulimall.cart.vo.CartVo;
import com.atguigu.gulimall.commons.bean.Constant;
import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuInfoVo;
import com.atguigu.gulimall.commons.utils.GuliJwtUtils;
import lombok.Data;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 * @create 2019-08-16 16:15
 */
@Service
public class CartServiceImpl  implements CartService {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    SkuFeignService skuFeignService;

    @Override
    public CartVo getCart(String userKey, String authorization) {

        CartKey cartKey = getKey(userKey,authorization);




        return null;
    }

    @Override
    public CartVo addToCart(Long skuId, Integer num, String userKey, String authorization) {

        CartKey cartKey = getKey(userKey, authorization);

        String key = cartKey.getKey();
        //从redis中查询 获取购物车
        RMap<Object, Object> map = redissonClient.getMap(Constant.CART_PREFIX + key);
        //添加之前先确认有没有该商品 如果有则num+1 如果没有添加
        String item = (String) map.get(skuId.toString());
        if (!StringUtils.isEmpty(item)){
            CartItemVo cartItemVo = JSON.parseObject(item, CartItemVo.class);
            cartItemVo.setNum(cartItemVo.getNum()+num);
            map.put(Constant.CART_PREFIX+key,item);
        }else {
            //远程调用查询sku的详情
            Resp<SkuInfoVo> skuInfoForCart = skuFeignService.getSkuInfoForCart(skuId);
            SkuInfoVo data = skuInfoForCart.getData();
            //拷贝数据
            CartItemVo cartItemVo = new CartItemVo();
            BeanUtils.copyProperties(data,cartItemVo);
            cartItemVo.setNum(num);
            map.put(Constant.CART_PREFIX+key,JSON.toJSONString(cartItemVo));

        }

        CartVo cartVo = new CartVo();
        cartVo.setUserKey(key);
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
            Long id = (Long) jwtBody.get("id");
            key  = id + "";
            //cartKey.setKey(key);
            cartKey.setLogin(true);
            if (!StringUtils.isEmpty(userKey)){
                //说明 可能存在需要合并的购物车数据
                cartKey.setMerge(true);
            }
        }else {
            //没有登录
            //如果第一次就有userkey
            if (StringUtils.isEmpty(userKey)){
                key = userKey;
                cartKey.setLogin(false);
                cartKey.setMerge(false);
            }else {
                //第一次就没有userkey就生成一个给前端
                key = UUID.randomUUID().toString().replace("-", "");
                cartKey.setTemp(true);

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
