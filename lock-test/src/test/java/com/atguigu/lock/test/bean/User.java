package com.atguigu.lock.test.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Administrator
 * @create 2019-08-15 23:40
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User{
    private String username;
    private String email;
}
