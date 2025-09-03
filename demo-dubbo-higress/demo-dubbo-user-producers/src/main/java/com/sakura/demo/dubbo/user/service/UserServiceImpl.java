package com.sakura.demo.dubbo.user.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.sakura.demo.dubbo.user.api.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.protocol.tri.rest.support.servlet.jakarta.ServletHttpRequestAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;

// docker run -d --name higress -v D:\ProgramData\docker\higress\data:/data -e O11Y=on -p 8001:8001 -p 8080:8080 -p 8443:8443  higress-registry.cn-hangzhou.cr.aliyuncs.com/higress/all-in-one:latest
@DubboService(timeout = 60000)
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public String sayHello(String name, String authHeader) {
        // 从RpcContext获取请求头
        log.info("authHeader:{}", authHeader);
        ServletHttpRequestAdapter request = (ServletHttpRequestAdapter)RpcContext.getServiceContext().getRequest(HttpServletRequest.class);
        log.info("request:{}", request);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info("headerName:{}", headerName);
        }

        StpUtil.login(1);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return "hello " + tokenInfo.getTokenValue();
    }

    @Override
    public String getInfo(String name) {
        log.info("name:{}", name);
        boolean login = StpUtil.isLogin();
        log.info("login:{}", login);
        return name;
    }
}
