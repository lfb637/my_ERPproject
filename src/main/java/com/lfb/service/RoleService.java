package com.lfb.service;

import com.lfb.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author lfb
 * @since 2021-02-17
 */
public interface RoleService extends IService<Role> {
      /**
       *  新增角色及角色所具有的资源
       * */
      boolean saveRole(Role role);
      boolean updateRole(Role role);
}
