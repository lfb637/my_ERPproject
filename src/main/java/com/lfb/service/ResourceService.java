package com.lfb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lfb.entity.Resource;
import com.lfb.vo.ResourceVO;
import com.lfb.vo.TreeVO;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author lfb
 * @since 2021-02-17
 */
public interface ResourceService extends IService<Resource> {
    /**
    * 根据角色id查询其资源
    * */
    List<ResourceVO> listResourceByRoleId(Long roleId);
    /**
     * 查询系统资源，供前端组件渲染
     * */
    List<TreeVO> listResource(Long roleId,Integer flag);
}
