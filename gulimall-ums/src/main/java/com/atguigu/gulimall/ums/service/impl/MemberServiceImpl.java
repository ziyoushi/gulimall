package com.atguigu.gulimall.ums.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.commons.bean.*;
import com.atguigu.gulimall.commons.exception.UserLoginException;
import com.atguigu.gulimall.commons.exception.UserRegisterException;
import com.atguigu.gulimall.ums.dao.MemberDao;
import com.atguigu.gulimall.ums.entity.MemberEntity;
import com.atguigu.gulimall.ums.service.MemberService;
import com.atguigu.gulimall.ums.vo.MemberLoginVo;
import com.atguigu.gulimall.ums.vo.MemberRegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public void registerUser(MemberRegisterVo vo) {
        MemberEntity memberEntity = new MemberEntity();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(vo.getPassword());

        memberEntity.setEmail(vo.getEmail());
        memberEntity.setPassword(encode);
        memberEntity.setUsername(vo.getUsername());
        memberEntity.setMobile(vo.getMobile());

        MemberEntity m1 = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("username", vo.getUsername()));
        if (m1 != null){
            throw new UserRegisterException(UserRegisterConstant.USER_NAME_EXCEPTION);
        }

        MemberEntity m2 = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("email", vo.getEmail()));
        if (m2 != null){
            throw new UserRegisterException(UserRegisterConstant.USER_EMAIL_EXCEPTION);
        }

        MemberEntity m3 = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("mobile", vo.getMobile()));
        if (m3 != null){
            throw new UserRegisterException(UserRegisterConstant.USER_PHONE_EXCEPTION);
        }

        memberDao.insert(memberEntity);
    }

    @Override
    public void login(MemberLoginVo vo) {

        //用户可以根据username phone email 来进行登录
        QueryWrapper<MemberEntity> wrapper = new QueryWrapper<MemberEntity>().eq("username", vo.getLoginacct())
                .or().eq("email", vo.getLoginacct())
                .or().eq("mobile", vo.getLoginacct());

        MemberEntity one = memberDao.selectOne(wrapper);

        if (one == null){
            //登录失败 没有该用户
            throw new UserLoginException(UserLoginConstant.USER_LOGIN_EXCEPTION);
        }

        //校验密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(vo.getPassword(), one.getPassword());
        if (matches){
            //密码校验成功
            //将用户信息保存到redis
            String token = UUID.randomUUID().toString().replace("-", "");
            redisTemplate.opsForValue().set(Constant.LOGIN_USER_PREFIX+token, JSON.toJSONString(one));


        }else {

            throw new UserLoginException(UserLoginConstant.USER_LOGIN_PASSWORD_EXCEPTION);
        }


    }

}