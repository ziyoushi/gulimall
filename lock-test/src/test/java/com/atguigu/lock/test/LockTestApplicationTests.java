package com.atguigu.lock.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LockTestApplicationTests {

    Map<String,Object> map = new HashMap<>();

    @Test
    public void contextLoads() {

    }

    public void write(){
    }

}

