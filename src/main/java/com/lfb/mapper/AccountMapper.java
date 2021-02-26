package com.lfb.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfb.entity.Account;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 账号表 Mapper 接口
 * </p>
 *
 * @author lfb
 * @since 2021-02-17
 */
public interface AccountMapper extends BaseMapper<Account> {
    /**
     *
     * 分页查询账号
     */
    //多个参数要加@Param注解
    IPage<Account> accountPage (Page<Account> page, @Param(Constants.WRAPPER) Wrapper<Account> wrapper);
    /**
     * 根据accountId查找账号信息
     * */
    Account selectAccountById(Long id);
}
