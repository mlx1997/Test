package com.mr.bo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;// 用户名
    
    private String password;// 密码

    private String phone;// 电话

    private Date created;// 创建时间

    private String salt;// 密码的盐值
}