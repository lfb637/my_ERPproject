package com.lfb.service.impl;

import com.lfb.entity.Customer;
import com.lfb.mapper.CustomerMapper;
import com.lfb.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author lfb
 * @since 2021-02-17
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

}
