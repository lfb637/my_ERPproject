package com.lfb.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lfb 2021/2/20
 * @Param ${Param}
 * 返回分页查询结果
 */
public class ResultUtil {
    public static R<Map<String,Object>> buildPage(IPage<?> mypage){
        HashMap<String, Object> data = new HashMap<>();
        data.put("count", mypage.getTotal());
        data.put("records", mypage.getRecords());
        return R.ok(data);
    }
    /**
     * 操作的反馈结果
     */
    public static R<Object> buildR(boolean success){
        if(success){    //操作成功
            return R.ok(null); //无需返回其他的数据
        }
        return R.failed("操作失败");
    }
}
