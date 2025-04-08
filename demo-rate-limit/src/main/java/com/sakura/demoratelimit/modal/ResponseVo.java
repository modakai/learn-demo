package com.sakura.demoratelimit.modal;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;

@Data
public class ResponseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    public static String responseJson() throws IOException {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setCode(400);
        responseVo.setMsg("请求过于频繁，请稍后再试");
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(responseVo);

    }
}
