package com.lfb.controller;


import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfb.entity.Account;
import com.lfb.entity.Role;
import com.lfb.query.AccountQuery;
import com.lfb.service.AccountService;
import com.lfb.service.RoleService;
import com.lfb.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
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
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    RoleService roleService;
    /**
     * 进入账户列表页
     * */
    @GetMapping("/toList")
    public String toList(){
        return "accountList";  //对应templates下的html文件名
    }
    /**
     * 查询账号列表
     * */
    @GetMapping("/list")
    @ResponseBody   //返回json数据
    public R<Map<String,Object>> list(AccountQuery query){
        /**三个查询字段*/
        QueryWrapper<Account> wrapper = Wrappers.query();
          wrapper.like(StringUtils.isNotBlank(query.getRealName()),"a.real_name", query.getRealName())
                  .like(StringUtils.isNotBlank(query.getEmail()),"a.email", query.getEmail());
        String createTimeRange = query.getCreateTimeRange();
        /**判定时间戳*/
        if(StringUtils.isNotBlank(createTimeRange)) {
            String[] timeArray = createTimeRange.split(" - "); //accountList.html中数据格式：placeholder=" - "
            wrapper.ge("a.create_name",timeArray[0]) /**ge：大于，le：小于*/
                    .le("a.create_name",timeArray[1]);
        }
        /**删除标识*/
        wrapper.eq("a.deleted", 0).orderByDesc("a.account_id");
        IPage<Account> myPage = accountService.accountPage(
                new Page<>(query.getPage(), query.getLimit()), wrapper);
        return ResultUtil.buildPage(myPage);
    }
    /**
     * 进入新增页
     * */
    @GetMapping("toAdd")
    public String toAdd(Model model) {    /**Model添加属性保证前端取到数据*/
         /**查询角色*/
        List<Role> roles = roleService.list(Wrappers.<Role>lambdaQuery().orderByAsc(Role::getRoleId));
        model.addAttribute("roles", roles); //包装为属性元素，前端可取
        return "accountAdd";
    }
    /**
     * 新增提交
     * */
    @PostMapping
    @ResponseBody
    public R<Object> add(@RequestBody Account account){
        setPasswordAndSalt(account);
        return ResultUtil.buildR(accountService.save(account));
    }
    /**
     * 进入修改页
     * */
    @GetMapping("toUpdate/{id}")  //带账户id
    public String toUpdate(@PathVariable Long id,Model model) {
        //查询账号
        Account account = accountService.getById(id);
        model.addAttribute("account", account);
        /**查询角色*/
        List<Role> roles = roleService.list(Wrappers.<Role>lambdaQuery().orderByAsc(Role::getRoleId));
        model.addAttribute("roles", roles); //包装为属性元素，前端可取
        return  "accountUpdate";
    }
    /**
     * 提交修改
     * */
    @PutMapping
    @ResponseBody   /**若失败需返回相应信息*/
    public R<Object> update(@RequestBody Account account){
        //如果密码不为空，则进行修改，否则此次修改为空
        if(StringUtils.isNotBlank(account.getPassword())){
            setPasswordAndSalt(account);
        }else{
            account.setPassword(null);
        }
        return ResultUtil.buildR(accountService.updateById(account));
    }
    /**
     * 删除账号，已登录账户不能删除
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public R<Object> delete(@PathVariable Long id, HttpSession session){
        Account account = (Account) session.getAttribute("account");
        if(account.getAccountId().equals(id)){  //已登录状态
            return R.failed("当前处于登录状态，删除失败");
        }
        return ResultUtil.buildR(accountService.removeById(id));
    }
    /**
     * 账号详情
     */
    @GetMapping("toDetail/{id}")
    public String toDetail(@PathVariable Long id, Model model) {
        Account account = accountService.getAccountById(id);
        model.addAttribute("account", account);
        return "accountDetail";
    }
    /**
     * 设置密码和密盐
     * */
    private void setPasswordAndSalt(Account account){
        //取出明文密码
        String password = account.getPassword();
        //执行加密
        String salt = UUID.fastUUID().toString().replaceAll("-","");
        MD5 md5 = new MD5(salt.getBytes());
        //执行加密
        String digestHex = md5.digestHex(password);
        account.setPassword(digestHex);
        account.setSalt(salt);
    }
    /**
     *  账号重名验证
     */
    /**对应accountAdd.html中的name="username"*/
    @GetMapping({"/{username}", "/{username}/{accountId}"})
    @ResponseBody
    /**函数名对应account.js中的函数名
     * 1、新增时用户名不能重复
     * 2、修改时可以改动自己的信息，但改动的用户名不能与其他的冲突
     * */
   public R<Object> checkUsername(@PathVariable String username,
                                   @PathVariable(required = false) Long accountId){
        Integer count = accountService.lambdaQuery()
                .eq(Account::getUsername, username)   //eq：equry查询，username：别名
                .ne(accountId!=null,Account::getAccountId,accountId)  //ne:not empty
                .count();
        return R.ok(count);
    }
}