package com.lfb.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfb.entity.Customer;
import com.lfb.service.CustomerService;
import com.lfb.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 客户表 前端控制器
 * </p>
 *
 * @author lfb
 * @since 2021-02-17
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    /**
    * 进入列表页
    * */
    @GetMapping("/toList")
    public String toList(){
           return "customerList";  //对应templates下的html文件名
    }
    /**
     * 查询
     * 查询条件：姓名or电话，查询页面，页面条数
     * */
    @GetMapping("/list")
    @ResponseBody   //返回json数据
    public R<Map<String,Object>> list(String realName,String phone,Long page,Long limit){
        LambdaQueryWrapper<Customer> wrapper = Wrappers.<Customer>lambdaQuery()
                .like(StringUtils.isNotBlank(realName), Customer::getRealName, realName)
                .like(StringUtils.isNotBlank(phone), Customer::getPhone, phone)
                .orderByDesc(Customer::getCustomerId);
        Page<Customer> mypage = customerService.page(new Page<>(page, limit), wrapper);
        return ResultUtil.buildPage(mypage);
    }
    /**
     * 进入新增页
     * */
    @GetMapping("toAdd")
    public String toAdd() {
       return "customerAdd";
    }
    /**
     * 新增提交
     * */
    @PostMapping
    @ResponseBody
    public R<Object> add(@RequestBody Customer customer){
        boolean success = customerService.save(customer);
        return ResultUtil.buildR(success);
        //return ResultUtil.buildR(customerService.save(customer));
    }
    /**
     * 进入修改页
     * */
    @GetMapping("toUpdate/{id}")  //带客户id
    public String toUpdate(@PathVariable Long id,Model model) {
        Customer customer = customerService.getById(id);
        model.addAttribute("customer", customer);
        return  "customerUpdate";
    }
    /**
     * 提交修改客户
     * */
    @PutMapping
    @ResponseBody   /**若失败需返回相应信息*/
    public R<Object> update(@RequestBody Customer customer){
        return ResultUtil.buildR(customerService.updateById(customer));
    }
    /**
     * 删除客户
    */
    @DeleteMapping("/{id}")
    @ResponseBody
    public R<Object> delete(@PathVariable Long id){
        return ResultUtil.buildR(customerService.removeById(id));
    }
    /**
     * 客户详情
     */
    @GetMapping("toDetail/{id}")
    public String toDetail(@PathVariable Long id, Model model){
        Customer customer = customerService.getById(id);
        model.addAttribute("customer", customer);
        return  "customerDetail";
    }
}
