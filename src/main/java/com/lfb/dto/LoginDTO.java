package com.lfb.dto;

import com.lfb.entity.Account;
import lombok.Data;

@Data
public class LoginDTO {
    /**
     * 重定向或跳转信息
     * */
    private String path;
    /**
     * 错误提示信息
     * */
    private String error;
    /**
     * 当前登录人信息
     * */
    private Account account;
}
