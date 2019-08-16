package com.atguigu.gulimall.cart.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-16 14:43
 */
public class CartVo {

    private Integer total;//总商品数量

    private BigDecimal totalPrice;//总商品价格

    private BigDecimal reductionPrice;//优惠了的价格

    private BigDecimal cartPrice;//购物车应该支付的价格

    @Setter @Getter
    private List<CartItemVo> items;//购物车中的所有购物项

    @Setter @Getter
    private String userKey;//临时用户的key

    //计算总商品数
    public Integer getTotal() {
        Integer num = 0;
        if (items != null && items.size() >0){
            for (CartItemVo item : items) {
                num += item.getNum();
            }
        }
        return num;
    }

    public BigDecimal getTotalPrice() {

        BigDecimal bigDecimal = new BigDecimal("0.0");
        if (items != null && items.size() >0){
            for (CartItemVo item : items) {
                bigDecimal = bigDecimal.add(item.getTotalPrice());
            }
        }

        return bigDecimal;
    }

    public BigDecimal getReductionPrice() {

        BigDecimal reduce = new BigDecimal("0.0");
        for (CartItemVo item : items) {

            //满减 打折
            List<SkuFullReductionVo> reductionVos = item.getReductionVos();
            if (reductionVos != null && reductionVos.size()>0){
                for (SkuFullReductionVo reductionVo : reductionVos) {
                    Integer type = reductionVo.getType();
                    //0打折 1 满减
                    //根据type进行分析
                    if (type == 0){
                        Integer fullCount = reductionVo.getFullCount();
                        Integer discount = reductionVo.getDiscount();
                        if (item.getNum() >= fullCount){
                            //折后价格
                            BigDecimal reduceTotalPrice = item.getPrice().multiply(new BigDecimal("0" + discount));
                            //减了多少钱
                            BigDecimal subtractPrice = item.getTotalPrice().subtract(reduceTotalPrice);
                            reduce = reduce.add(subtractPrice);
                        }

                    }

                    if (type == 1){
                        BigDecimal fullPrice = reductionVo.getFullPrice();
                        BigDecimal reducePrice = reductionVo.getReducePrice();
                        //总价格个fullPrice进行比较 总价格减fullPrice与0进行比较
                        if (item.getTotalPrice().subtract(fullPrice).compareTo(new BigDecimal("0.0")) > -1){
                            reduce = reduce.add(reducePrice);
                        }

                    }

                }
            }

            //优惠券
            List<SkuCouponVo> couponVos = item.getCouponVos();
            if (couponVos != null && couponVos.size() >0){
                for (SkuCouponVo couponVo : couponVos) {
                    BigDecimal amount = couponVo.getAmount();
                    reduce = reduce.add(amount);
                }
            }

        }

        return reduce;
    }
}
