package com.atguigu.gulimall.cart.conroller;

import com.atguigu.gulimall.cart.service.CartService;
import com.atguigu.gulimall.cart.vo.CartVo;
import com.atguigu.gulimall.commons.bean.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Administrator
 * @create 2019-08-16 16:09
 */
@Api(tags = "购物车系统")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    ThreadPoolExecutor executor;

    //关闭其他线程池
    // 在业务运行期间通过运维平台控制 关闭一些资源
    @GetMapping("/stop/other")
    public Resp<Object> closeThreadPool(){

        executor.shutdown();//等到未执行的线程执行完后 关闭线程池

        Map<String,Object> map = new HashMap<>();
        map.put("closeQueue",executor.getQueue().size());
        map.put("waitActiveCount",executor.getActiveCount());

        return Resp.ok(map);
    }

    //选择购物车 选中 不选中
    @ApiOperation("选中/不选中购物车")
    @PostMapping("/check")
    public Resp<CartVo> checkCart(@RequestParam("skuIds") Long[] skuIds,
                                  @RequestParam("status") Integer status,
                                  @RequestParam(value = "userKey",required = false) String userKey,
                                  @RequestHeader(name = "Authorization",required = false) String authorization){

       CartVo cartVo = cartService.checkCart(skuIds,status,userKey,authorization);

        return Resp.ok(cartVo);
    }



    //修改购物车
    @ApiOperation("更新购物车商品数量")
    @PostMapping("/update")
    public Resp<CartVo> updateCart(@RequestParam(value = "skuId",required = true) Long skuId,
                                   @RequestParam(value = "num",defaultValue = "1") Integer num,
                                   @RequestParam(value = "userKey",required = false) String userKey,
                                   @RequestHeader(name = "Authorization",required = false) String authorization){

        CartVo cartVo = cartService.updateCart(skuId,num,userKey,authorization);

        return Resp.ok(cartVo);
    }


    @ApiOperation("获取购物车信息")
    @GetMapping("/list")
    public Resp<CartVo> getCart(@RequestParam(value = "userKey",required = false) String userKey,
                                @RequestHeader(name = "Authorization",required = false) String authorization) throws ExecutionException, InterruptedException {

        CartVo cartVo = cartService.getCart(userKey,authorization);

        return Resp.ok(cartVo);
    }

    //将某个sku添加到购物车
    @GetMapping("/add")
    public Resp<CartVo> addToCart(@PathVariable("skuId") Long skuId, Integer num,
                                  @RequestParam(value = "userKey",required = false) String userKey,
                                  @RequestHeader(name = "Authorization",required = false) String authorization) throws ExecutionException, InterruptedException {

        CartVo cartVo = cartService.addToCart(skuId,num,userKey,authorization);

        return Resp.ok(cartVo);
    }


}
