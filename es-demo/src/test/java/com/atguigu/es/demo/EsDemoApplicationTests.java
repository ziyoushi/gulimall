package com.atguigu.es.demo;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsDemoApplicationTests {

    @Autowired
    JestClient jestClient;

    @Test
    public void contextLoads() throws IOException {

        User user = new User(UUID.randomUUID().toString().substring(0,5), "zhangsan@qq.com", 28);

        //1、获取一个Index动作的建造者，
        Index.Builder builder = new Index.Builder(user)
                .index("user")
                .type("info");

        //2、构造出这个index动作
        Index index = builder.build();

        //3、执行这个动作
        DocumentResult result = jestClient.execute(index);

        //4、打印结果
        System.out.println("刚才保存的是："+result.getId());
        System.out.println("刚才保存数据版本是："+result.getValue("_version"));
    }

}

@NoArgsConstructor
@AllArgsConstructor
@Data
class User{
    private String username;
    private String email;
    private Integer age;
}
