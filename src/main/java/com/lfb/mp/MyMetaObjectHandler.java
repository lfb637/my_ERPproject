package com.lfb.mp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lfb.entity.Account;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;

/**
 * @author lfb 2021/2/21
 * @Param ${Param}
 * 当在主界面添加记录时，往数据库自定义填充页面未添加的字段
 * mybatis-plus文档:https://baomidou.com/guide/auto-fill-metainfo.html
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        if(metaObject.hasSetter("createTime")) {
            this.strictInsertFill(metaObject, "createTime",
                    LocalDateTime.class, LocalDateTime.now());
        }
        if(metaObject.hasSetter("createTime")) {
            //添加账户ID信息，通过从登录的session获取
            Object account = RequestContextHolder.getRequestAttributes().getAttribute(
                    "account", RequestAttributes.SCOPE_SESSION
            );
            if (account != null) {
                Long accountId = ((Account) account).getAccountId();
                this.strictInsertFill(metaObject, "createAccountId",
                        Long.class, accountId);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if(metaObject.hasSetter("modifiedTime")) {
            this.strictUpdateFill(metaObject, "modifiedTime",
                    LocalDateTime.class, LocalDateTime.now());
        }
        if(metaObject.hasSetter("modifiedTime")) {
            //添加账户ID信息，通过从登录的session获取
            Object account = RequestContextHolder.getRequestAttributes().getAttribute(
                    "account", RequestAttributes.SCOPE_SESSION
            );
            if (account != null) {
                Long accountId = ((Account) account).getAccountId();
                this.strictUpdateFill(metaObject, "modifiedAccountId",
                        Long.class, accountId);
            }
        }
    }
}