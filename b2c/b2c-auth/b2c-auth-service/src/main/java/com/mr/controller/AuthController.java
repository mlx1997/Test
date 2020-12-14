package com.mr.controller;

import com.mr.bo.UserInfo;
import com.mr.config.JwtConfig;
import com.mr.service.AuthService;
import com.mr.utils.CookieUtils;
import com.mr.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping("login")
    public ResponseEntity<Void> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ){

        String auth = authService.auth(username, password);

        if(auth == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

//        @param request 请求对象
//	 * @param response 响应对象
//	 * @param cookieName cookie名称
//	 * @param cookieValue cookie值
//	 * @param cookieMaxage cookie过期时间
//	 * @param isEncode 是否按照ut8编码

        CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),auth, jwtConfig.getCookieMaxAge(),true);

        return ResponseEntity.ok(null);
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(
            @CookieValue("B2C_TOKEN") String token,
            HttpServletRequest request,
            HttpServletResponse response
    ){

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());

            String s = JwtUtils.generateToken(userInfo, jwtConfig.getPrivateKey(), jwtConfig.getExpire());

            CookieUtils.setCookie(request, response, jwtConfig.getCookieName(),s, jwtConfig.getCookieMaxAge(),true);

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        }

    }

}
