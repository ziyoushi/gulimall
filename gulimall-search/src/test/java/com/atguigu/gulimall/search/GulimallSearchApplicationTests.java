package com.atguigu.gulimall.search;

import io.searchbox.client.JestClient;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallSearchApplicationTests {

    @Autowired
    private JestClient jestClient;

    @Test
    public void contextLoads() throws IOException {

        User user = new User("zhangsan", "zhang@qq.com", 23);

        Index build = new Index.Builder(user).index("user").type("info").id("1").build();

        jestClient.execute(build);

        System.out.println("保存完成");
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
