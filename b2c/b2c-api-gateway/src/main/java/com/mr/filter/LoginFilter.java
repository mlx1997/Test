package com.mr.filter;

import com.mr.config.FilterProperties;
import com.mr.config.JwtConfig;
import com.mr.utils.CookieUtils;
import com.mr.utils.JwtUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@EnableConfigurationProperties({JwtConfig.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtConfig jwtConfig;

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();

//        return !this.isAllowPath(request.getRequestURI());
        return false;
    }

    public boolean isAllowPath(String requestURI){

        boolean result=false;

        for(String uri:filterProperties.getAllowPaths()){

            if(requestURI.startsWith(uri)){
                result = true;
                break;
            }
        }

        return result;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();
        System.out.println("拦截到请求"+request.getRequestURI());
        // 获取token
        String token = CookieUtils.getCookieValue(request, jwtConfig.getCookieName());
        System.out.println("token信息"+token);
        // 校验
        try {
        // 通过公钥解密，如果成功，就放行，失败就拦截
//            JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
        } catch (Exception e) {
            System.out.println("解析失败 拦截"+token);
            // 校验出现异常，返回403
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
        }
        return null;
    }
}