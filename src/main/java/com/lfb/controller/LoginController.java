package com.lfb.controller;


import com.lfb.dto.LoginDTO;
import com.lfb.service.AccountService;
import com.lfb.service.ResourceService;
import com.lfb.vo.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author lfb
 * @since 2021-02-17
 */
@Controller
@RequestMapping("/auth")
public class LoginController {
       /**
        * 用户登录
        * @param username
        * @param password
        * @return
        * */
       @Autowired
       private AccountService accountService;
       @Autowired
       private ResourceService resourceService;
       @PostMapping("login") /**响应前端请求*/
       public String login(String username, String password, HttpSession session,
                           RedirectAttributes attributes,Model model){  //model：目录以及其子菜单
           LoginDTO loginDTO = accountService.login(username,password);
           String error = loginDTO.getError();
           if(error==null){
              /**
               * 保存登录信息
               * */
               session.setAttribute("account", loginDTO.getAccount());
               /**登录成功则显示登录人你所拥有的资源菜单*/
               List<ResourceVO> resourceVOS = resourceService.listResourceByRoleId(loginDTO.getAccount().getRoleId());
               model.addAttribute("resource",resourceVOS);
           }else{
            /**
             * 返归具体错误信息，并重定向
             * */
               attributes.addFlashAttribute("error", error);
           }
           /**正常访问则跳转到其他页面:路径在AccountServiceImpl中设置*/
           return loginDTO.getPath();
       }
       /**
        * 登出请求
        * */
       @GetMapping("logout")
       public String logout(HttpSession session){
           session.invalidate();
           return "redirect:/"; //重定向到初始登录页面
       }
}
