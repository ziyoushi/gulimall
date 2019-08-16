package com.atguigu.gulimall.ums.service;

import com.atguigu.gulimall.ums.vo.MemberLoginVo;
import com.atguigu.gulimall.ums.vo.MemberRegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.ums.entity.MemberEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;


/**
 * 会员
 *
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-01 21:24:21
 */
public interface MemberService extends IService<MemberEntity> {

    PageVo queryPage(QueryCondition params);

    //注册
    void registerUser(MemberRegisterVo vo);

    //用户登录 todo 修改返回值
    void login(MemberLoginVo vo);
}

