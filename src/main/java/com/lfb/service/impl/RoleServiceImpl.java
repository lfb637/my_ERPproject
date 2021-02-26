package com.lfb.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfb.entity.Role;
import com.lfb.entity.RoleResource;
import com.lfb.mapper.RoleMapper;
import com.lfb.mapper.RoleResourceMapper;
import com.lfb.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author lfb
 * @since 2021-02-17
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    RoleResourceMapper roleResourceMapper;
    /**
     *  新增角色及角色所具有的资源
     * */
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)//执行回滚
    public boolean saveRole(Role role) {
        save(role);
        //取角色id
        Long roleId = role.getRoleId();
        //取资源ID
        List<Long> resourceIds = role.getResourceIds();
        if (CollectionUtil.isNotEmpty(resourceIds)) {
            //往角色资源表里插数据
            for (Long resourceId : resourceIds) {
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resourceId);
                roleResourceMapper.insert(roleResource);
            }
        }
        return true;
    }
    /**
     * 修改角色及角色所具有的资源
     * */
    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)//执行回滚
    public boolean updateRole(Role role) {
        updateById(role);
        //取角色id
        Long roleId = role.getRoleId();
        //先做删除处理，再重新添加
        roleResourceMapper.delete(
                Wrappers.<RoleResource>lambdaQuery().eq(RoleResource::getRoleId,roleId));
        //取资源ID
        List<Long> resourceIds = role.getResourceIds();
        if (CollectionUtil.isNotEmpty(resourceIds)) {
            //往角色资源表里插数据
            for (Long resourceId : resourceIds) {
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(roleId);
                roleResource.setResourceId(resourceId);
                roleResourceMapper.insert(roleResource);
            }
        }
        return true;
    }

}
