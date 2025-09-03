package com.sakura.demo.dubbo.user.filter;


import org.apache.dubbo.rpc.*;

public class AppendedFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result= invoker.invoke(invocation);
        // Obtain the returned value
        Result appResponse = ((AsyncRpcResult) result).getAppResponse();
        // Appended value
        if (appResponse.getValue() instanceof String) {
            appResponse.setValue(appResponse.getValue() + "'s customized AppendedFilter");
        }
        return result;
    }
}