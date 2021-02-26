package com.lfb.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.lfb.entity.Resource;
import com.lfb.vo.ResourceVO;
import com.lfb.vo.TreeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author lfb
 * @since 2021-02-17
 */

public interface ResourceMapper extends BaseMapper<Resource> {
    /**
    * 查询当前登录人的资源
    * */
    List<ResourceVO> listResource(@Param(Constants.WRAPPER) Wrapper<Resource> wrapper);
    List<TreeVO> listResourceByRoleId(@Param(Constants.WRAPPER) Wrapper<Resource> wrapper
            ,@Param("roleId") Long roleId);
}
