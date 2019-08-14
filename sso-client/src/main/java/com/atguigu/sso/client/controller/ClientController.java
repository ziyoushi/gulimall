package com.atguigu.sso.client.controller;

import com.atguigu.sso.client.util.GuliJwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * @create 2019-08-14 12:32
 */
@Controller
public class ClientController {

    @Value("${sso.serverName}")
    private String serverName;

    @RequestMapping("/see")
    public String see(HttpServletRequest request,
                      @CookieValue(value = "atguigusso", required = false) String ssocookie,
                      @RequestParam(value = "atguigusso", required = false) String ssoparam,
                      Model model,
                      HttpServletResponse response) {

        StringBuffer url = request.getRequestURL();

        try {

            //参数优先
            if (!StringUtils.isEmpty(ssoparam)) {
                //使用jwt工具类校验
                GuliJwtUtils.checkJwt(ssoparam);
                //校验 去成功页面
                Cookie cookie = new Cookie("atguigusso", ssoparam);
                response.addCookie(cookie);
                return "success";
            }

            if (!StringUtils.isEmpty(ssocookie)) {

                //校验成功后 去成功页面
                GuliJwtUtils.checkJwt(ssocookie);

                return "success";

            }

            throw new NullPointerException();

        } catch (NullPointerException e) {
            //如果参数为空 cookie为空 去登录页进行登录

            return "redirect:" + serverName + "?redirect_url=" + url.toString();
        }catch (Exception e){
            System.out.println("令牌校验失败");
            return "error";
        }

    }

    @GetMapping("/say")
    public String say() {
        return "hello";
    }

}
