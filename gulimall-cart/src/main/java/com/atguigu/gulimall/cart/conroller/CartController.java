package com.atguigu.gulimall.cart.conroller;

import com.atguigu.gulimall.cart.service.CartService;
import com.atguigu.gulimall.cart.vo.CartVo;
import com.atguigu.gulimall.commons.bean.Resp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 * @create 2019-08-16 16:09
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @ApiOperation("获取购物车信息")
    @GetMapping("/list")
    public Resp<CartVo> getCart(@RequestParam(value = "userKey",required = false) String userKey,
                                @RequestHeader(name = "Authorization",required = false) String authorization){

        CartVo cartVo = cartService.getCart(userKey,authorization);

        return Resp.ok(cartVo);
    }

    //将某个sku添加到购物车
    @GetMapping("/add")
    public Resp<CartVo> addToCart(@PathVariable("skuId") Long skuId, Integer num,
                                  @RequestParam(value = "userKey",required = false) String userKey,
                                  @RequestHeader(name = "Authorization",required = false) String authorization){

        CartVo cartVo = cartService.addToCart(skuId,num,userKey,authorization);

        return Resp.ok(cartVo);
    }


}
