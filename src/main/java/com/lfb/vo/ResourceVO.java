package com.lfb.vo;

import lombok.Data;

import java.util.List;

@Data
public class ResourceVO {
    /**
     * 资源对象
     * */
    /**
     * 主键
     */    private Long resourceId;

    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 请求地址
     */
    private String url;
    /**
    * 子菜单
    * */
    private List<ResourceVO> subs;
}
