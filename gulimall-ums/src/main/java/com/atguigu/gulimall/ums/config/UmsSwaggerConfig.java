package com.atguigu.gulimall.ums.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Administrator
 * @create 2019-08-02 10:14
 */
@Configuration
public class UmsSwaggerConfig {

    @Value("${swagger2.enable:false}")
    private boolean enable = false;

    @Bean("用户平台")
    public Docket projectApis() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户平台")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.regex("/ums.*"))
                .build()
                .apiInfo(apiInfo())
                .enable(enable);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("谷粒商城-用户平台接口文档")
                //.description("提供用户模块/审核模块/项目模块/支付模块的文档")
                .termsOfServiceUrl("http://www.atguigu.com/")
                .version("1.0")
                .build();
    }
}
