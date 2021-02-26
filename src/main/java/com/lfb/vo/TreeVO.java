package com.lfb.vo;

import lombok.Data;

import java.util.List;
/**
 * 角色列表展示需要组件元素
 * layui文档：https://www.layui.com/doc/modules/tree.html#onclick
 * */
@Data
public class TreeVO {

    private String title;

    private Long id;

    private boolean checked;

    private List<TreeVO> children; // 子菜单
}
