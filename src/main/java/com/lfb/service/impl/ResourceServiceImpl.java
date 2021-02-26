package com.lfb.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfb.entity.Resource;
import com.lfb.mapper.ResourceMapper;
import com.lfb.service.ResourceService;
import com.lfb.vo.ResourceVO;
import com.lfb.vo.TreeVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author lfb
 * @since 2021-02-17
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Override
    public List<ResourceVO> listResourceByRoleId(Long roleId) {
        /**条件构造器，动态渲染资源列表*/
        QueryWrapper<Resource> query = Wrappers.<Resource>query();
        /**查询一级目录*/
        query.eq("rr.role_id",roleId)
                .isNull("re.parent_id")
                .orderByAsc("re.sort");
        List<ResourceVO> resourceVOS = baseMapper.listResource(query);
         /**查询对应目录的子资源*/
         resourceVOS.forEach(r->{
             Long resourceId = r.getResourceId();
             QueryWrapper<Resource> subWrapper = Wrappers.<Resource>query();
             subWrapper
                     .eq("role_id",roleId)
                     .eq("re.parent_id",resourceId)
                     .orderByAsc("re.sort"); /**父id即为上级id*/
             List<ResourceVO> subResourceVOS = baseMapper.listResource(subWrapper);
             /**查询到目录的下级菜单不为空则进行渲染*/
             if(CollectionUtil.isNotEmpty(subResourceVOS)){
                 r.setSubs(subResourceVOS);
             }
         });
        return resourceVOS;
    }

    @Override
    public List<TreeVO> listResource(Long roleId, Integer flag) {
        //查询父类资源不为空
        if(roleId==null) { //新增
            LambdaQueryWrapper<Resource> wrapper = Wrappers.<Resource>lambdaQuery()
                    .isNull(Resource::getParentId).orderByAsc(Resource::getSort);
            List<Resource> resources = list(wrapper);
            List<TreeVO> treeVOS = resources.stream().map(r -> {
                TreeVO treeVO = new TreeVO();
                treeVO.setId(r.getResourceId());
                treeVO.setTitle(r.getResourceName());
                //查询父组件下的子组件
                LambdaQueryWrapper<Resource> subWrapper = Wrappers.<Resource>lambdaQuery()
                        .eq(Resource::getParentId, r.getResourceId())
                        .orderByAsc(Resource::getSort);
                List<Resource> subResources = list(subWrapper);
                //子组件不为空，
                if (CollectionUtil.isNotEmpty(subResources)) {
                    List<TreeVO> children = subResources.stream().map(sub -> {
                        TreeVO subTreeVO = new TreeVO();
                        subTreeVO.setId(sub.getResourceId());
                        subTreeVO.setTitle(sub.getResourceName());
                        return subTreeVO;
                    }).collect(Collectors.toList());
                    treeVO.setChildren(children);
                }
                return treeVO;
            }).collect(Collectors.toList());
            return treeVOS;
        } else {  //修改or查看（flag=1为查看功能，0为修改)_
            QueryWrapper<Resource> query = Wrappers.<Resource>query();
            query.eq(flag==1,"rr.role_id",roleId)
            .isNull("re.parent_id").orderByAsc("re.sort");
            List<TreeVO> treeVOS = baseMapper.listResourceByRoleId(query,roleId);
            treeVOS.forEach(t->{
                t.setChecked(false);  //防止对菜单资源上级选中其下级也都选中
                Long id = t.getId();
                QueryWrapper<Resource> subWrapper = Wrappers.<Resource>query();
                subWrapper
                        .eq(flag==1,"rr.role_id",roleId)
                        .eq("re.parent_id",id)
                        .orderByAsc("re.sort");
                List<TreeVO> children = baseMapper.listResourceByRoleId(subWrapper,roleId);
                if(CollectionUtil.isNotEmpty(children)) {
                    t.setChildren(children);
                }
            });
            return treeVOS;
        }
    }
}
