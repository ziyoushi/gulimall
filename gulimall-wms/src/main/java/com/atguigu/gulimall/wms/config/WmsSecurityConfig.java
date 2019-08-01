package com.atguigu.gulimall.wms.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Administrator
 * @create 2019-08-01 23:23
 */
public class WmsSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //关闭csrf
        http.csrf().disable();

        http.authorizeRequests().antMatchers("/**").permitAll();
    }
}
