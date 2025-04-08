package com.sakura.demoratelimit.modal;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;
}
