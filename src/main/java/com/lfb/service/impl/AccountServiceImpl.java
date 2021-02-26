package com.lfb.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfb.dto.LoginDTO;
import com.lfb.entity.Account;
import com.lfb.mapper.AccountMapper;
import com.lfb.service.AccountService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账号表 服务实现类
 * </p>
 *
 * @author lfb
 * @since 2021-02-17
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Override
    public LoginDTO login(String username, String password) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPath("redirect:/");
        /**查询一条数据eq(equal query): getUsername(页面输入)==username(数据库字段值)*/
        Account account = lambdaQuery().eq(Account::getUsername, username).one();
        if(account==null){
            loginDTO.setError("用户名不存在！");
            return loginDTO;
        }
        if(account.getPassword()==null) {
            loginDTO.setError("密码错误！");
            return loginDTO;
        }
        MD5 md5 = new MD5(account.getSalt().getBytes());
        String digestHex = md5.digestHex(password);
        if(!digestHex.equals(account.getPassword())){
            loginDTO.setError("密码错误！");
            return loginDTO;
        }
        loginDTO.setAccount(account);
        loginDTO.setPath("main");
        return loginDTO;
    }
    /**
     * 当前页的账户数量
     * */
    @Override
    public IPage<Account> accountPage(Page<Account> page, Wrapper<Account> wrapper) {
        return baseMapper.accountPage(page, wrapper);
    }
    /**
     * 通过id查找账户
     * */
    @Override
    public Account getAccountById(Long id) {
        return  baseMapper.selectAccountById(id);
    }

}