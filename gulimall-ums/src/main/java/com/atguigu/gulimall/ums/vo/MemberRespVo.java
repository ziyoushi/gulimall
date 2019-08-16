package com.atguigu.gulimall.ums.vo;

import lombok.Data;

/**
 * @author Administrator
 * @create 2019-08-16 9:55
 */
@Data
public class MemberRespVo {

    private String username;
    private String email;
    private String header;
    private String sign;
    private String mobile;
    private Long levelId;

    //前端需要的访问令牌
    private String token;

}
