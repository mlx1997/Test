package com.mr;

import com.mr.utils.JwtUtils;
import com.mr.utils.RsaUtils;
import com.mr.bo.UserInfo;
import com.mr.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTokenTest {

    //公钥位置
    private static final String pubKeyPath = "D:\\mr\\upload\\reskey\\rea.pub";
    //私钥位置
    private static final String priKeyPath = "D:\\mr\\upload\\reskey\\rea.pri";
    //公钥对象
    private PublicKey publicKey;
    //私钥对象
    private PrivateKey privateKey;


    /**
     * 生成公钥私钥 根据密文
     * @throws Exception
     */
    @Test
    public void genRsaKey() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "1234");
    }


    /**
     * 从文件中读取公钥私钥
     * @throws Exception
     */
    @Before
    public void getKeyByRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    /**
     * 根据用户信息结合私钥生成token
     * @throws Exception
     */
    @Test
    public void genToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(1l, "tomcat"), privateKey, 2);
        System.out.println("user-token = " + token);
    }


    /**
     * 结合公钥解析token
     * @throws Exception
     */
    @Test
    public void parseToken() throws Exception {

        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJ0b21jYXQiLCJleHAiOjE2MDY0ODQwMzN9.fcod4w2vUQn04K-9f8eHrLiNz6Yws4-ZIhJ9r_XWt9OYZKiTLVIk2_z-TxpjgJHdHXfLe6ozXs-tlhRPmnGjlLYR10rtteyp2I25ztp0dP3pmw2EMc-2N1LQaX1R40WOoQknVnPHqa2b947Xb7gwFGE4maMKmUjKsHehy5ZUqDU";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}


