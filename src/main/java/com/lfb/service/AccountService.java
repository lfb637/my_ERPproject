package com.lfb.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfb.dto.LoginDTO;
import com.lfb.entity.Account;

/**
 * <p>
 * 账号表 服务类
 * </p>
 *
 * @author lfb
 * @since 2021-02-17
 */
public interface AccountService extends IService<Account> {
       LoginDTO login(String username, String password);
       IPage<Account> accountPage (Page<Account> page, Wrapper<Account> wrapper);
       Account getAccountById(Long id);
}
