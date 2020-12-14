package com.mr.service;

import com.mr.bo.UserInfo;
import com.mr.client.UserClient;
import com.mr.config.JwtConfig;
import com.mr.bo.User;
import com.mr.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtConfig jwtConfig;

    public String auth(String username, String password) {

        User user = userClient.query(username, password);

        if(user == null){
            return null;
        }

        UserInfo userInfo = new UserInfo(user.getId(), user.getUsername());

//        @param userInfo      载荷中的数据
//     * @param privateKey    私钥
//     * @param expireMinutes 过期时间，单位秒
        try {
            String token=JwtUtils.generateToken(userInfo , jwtConfig.getPrivateKey(),jwtConfig.getExpire());
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
