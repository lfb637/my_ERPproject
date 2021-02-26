package com.lfb.query;

import lombok.Data;

/**
 * @author lfb 2021/2/21
 * @Param ${Param}
 */
@Data
public class AccountQuery {
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 创建时间
     */
    private String createTimeRange;
    /**
     * 分页
     */
    private Long page;
    private Long limit;

}
