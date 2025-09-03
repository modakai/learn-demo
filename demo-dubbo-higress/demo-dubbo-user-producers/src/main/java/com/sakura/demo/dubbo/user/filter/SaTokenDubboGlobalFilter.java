package com.sakura.demo.dubbo.user.filter;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.servlet.model.SaRequestForServlet;
import cn.dev33.satoken.servlet.model.SaResponseForServlet;
import cn.dev33.satoken.servlet.model.SaStorageForServlet;
import cn.dev33.satoken.servlet.util.SaTokenContextJakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.remoting.http12.HttpRequest;
import org.apache.dubbo.remoting.http12.HttpResponse;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.protocol.tri.rest.filter.RestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaTokenDubboGlobalFilter implements Filter, RestFilter {

    private static final Logger log = LoggerFactory.getLogger(SaTokenDubboGlobalFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        log.info("AAAA");
        HttpServletRequest request = RpcContext.getServiceContext().getRequest(HttpServletRequest.class);
        HttpServletResponse response = RpcContext.getServiceContext().getResponse(HttpServletResponse.class);
        SaTokenContextJakartaServletUtil.setContext(request, response);

        return invoker.invoke(invocation);
    }

    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) throws Exception {
        log.info("AAAA");

        SaRequest req = new SaRequestForServlet((HttpServletRequest) request);
        SaResponse res = new SaResponseForServlet((HttpServletResponse) response);
        SaStorage stg = new SaStorageForServlet((HttpServletRequest) request);
        SaManager.getSaTokenContext().setContext(req, res, stg);
        chain.doFilter(request, response);
    }
}
