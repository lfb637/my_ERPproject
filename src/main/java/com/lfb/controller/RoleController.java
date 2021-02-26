package com.lfb.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfb.entity.Account;
import com.lfb.entity.Role;
import com.lfb.service.AccountService;
import com.lfb.service.ResourceService;
import com.lfb.service.RoleService;
import com.lfb.utils.ResultUtil;
import com.lfb.vo.TreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author lfb
 * @since 2021-02-17
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    @Autowired
    ResourceService resourceService;
    @Autowired
    AccountService accountService;
    /**
     * 进入角色列表页
     * */
    @GetMapping("/toList")
    public String toList(){
        return "roleList";  //对应templates下的html文件名
    }
    /**
     * 查询
     * 查询条件：姓名or电话，查询页面，页面条数
     * */
    @GetMapping("/list")
    @ResponseBody   //返回json数据
    public R<Map<String,Object>> list(String roleName, Long page, Long limit){
        LambdaQueryWrapper<Role> wrapper = Wrappers.<Role>lambdaQuery()
                .like(StringUtils.isNotBlank(roleName), Role::getRoleName, roleName)
                .orderByDesc(Role::getRoleId);
        Page<Role> mypage = roleService.page(new Page<>(page, limit), wrapper);
        return ResultUtil.buildPage(mypage);
    }
    /**
     * 进入新增页
     * */
    @GetMapping("toAdd")
    public String toAdd() {
        return "roleAdd";
    }
    /**
     * 新增提交
     * */
    @PostMapping
    @ResponseBody
    public R<Object> add(@RequestBody Role role){
        boolean success = roleService.saveRole(role);
        return ResultUtil.buildR(success);
    }
    /**
     * 进入修改页
     * */
    @GetMapping("toUpdate/{id}")  //带客户id
    public String toUpdate(@PathVariable Long id, Model model) {
        Role role = roleService.getById(id);
        model.addAttribute("role", role);
        return  "roleUpdate";
    }
    /**
     * 提交修改角色
     * */
    @PutMapping
    @ResponseBody   /**若失败需返回相应信息*/
    public R<Object> update(@RequestBody Role role){
        return ResultUtil.buildR(roleService.updateRole(role));
    }
    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public R<Object> delete(@PathVariable Long id){
        /**多个账号对持有同一角色的删除
         * 若有账号持有则不能删除
         * */
        Integer count = accountService.lambdaQuery().eq(Account::getRoleId, id).count();
        if(count>0){
             R.failed("正在有账号使用该资源，拒绝删除");
        }
        return ResultUtil.buildR(roleService.removeById(id));
    }
    /**
     * 角色详情
     */
    @GetMapping("toDetail/{id}")
    public String toDetail(@PathVariable Long id, Model model){
        Role role = roleService.getById(id);
        model.addAttribute("role", role);
        return  "roleDetail";
    }
    /**
     * 获取角色资源列表,请求资源路径，
     * 匹配role.js中showTree('/role/listResource/'+roleId+'/0','resource');
     * */
    @GetMapping({"listResource", "listResource/{roleId}","listResource/{roleId}/{flag}"})
    @ResponseBody
    public R<List<TreeVO>> listResource (@PathVariable(required = false) Long roleId
            ,@PathVariable(required = false) Integer flag){
       return R.ok(resourceService.listResource(roleId,flag));
    }
}
