package com.atguigu.sso.server.controller;

import com.atguigu.sso.server.utils.GuliJwtUtils;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 * @create 2019-08-14 12:39
 */
@Controller
public class SsoServerController {

    @GetMapping("/ssoServer")
    public String ssoServer(){

        return null;
    }

    //登录页面
    @RequestMapping("/login.html")
    public String toLoginPage(@RequestParam(value = "redirect_url",required = false) String redirectUrl,
                              @CookieValue(value = "atguigusso",required = false) String osscookie,
                              Model model){


        //查看是否sso server是否带有cookie如果没有 则去登录
        if (StringUtils.isEmpty(osscookie)){

            model.addAttribute("url",redirectUrl);

            return "login";
        }else {
            //已经登录 带上sso 端的cookie
            return "redirect:"+redirectUrl+"?atguigusso="+osscookie;
        }

    }

    //登录
    @PostMapping("/doLogin")
    public String doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("url") String url, Model model,
                          HttpServletResponse response){

        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){

            String token = UUID.randomUUID().toString().substring(0, 6);

            //设置jwt的负载信息
            Map<String,Object> loginUser = new HashMap<>();
            loginUser.put("name",username);
            loginUser.put("email",username+"@qq.com");
            loginUser.put("token",token);

            DefaultClaims claims = new DefaultClaims();

            //构建jwt
            String jwt = GuliJwtUtils.buildJwt(loginUser, claims);

            //sso中心服务端设置cookie
            Cookie cookie = new Cookie("atguigusso",jwt);
            response.addCookie(cookie);

            //登录成功 跳到客户端
            return "redirect:" + url + "?atguigusso="+jwt;

        }else {

            model.addAttribute("username",username);
            model.addAttribute("url",url);

            //如果失败 继续登录 并把值带回去
            return "forward:/login.html";
        }


    }


}
