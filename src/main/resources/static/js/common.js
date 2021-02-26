/**
 *通用弹出框
 */
function openLayer(url,title) {    //url对应controller中的访问路径如：/customer/toAdd
    $.ajaxSettings.async = false;  //为保证新增时页面能正常渲染
    $.get(url,function (res) {
        layer.open({
            type:1 //1表示页面层
            ,title:title
            ,area:['800px','450px']
            ,content:res
        });
    });
    $.ajaxSettings.async = true;
}

/**
 * 监听提交事件
 * */
function mySubmit(filter,type,func) {   /**filter对应...Add.html中lay-filter="addSubmit"的"addSubmit"*/
    layui.form.on('submit('+filter+')', function(data){
        if(typeof(func) != 'undefined') {
            func(data.field);          //表单数据键值动态追加属性
        }
        $.ajax({
            url:data.form.action
            ,async:false
            ,type:type
            ,contentType:'application/json;charset=utf-8'
            ,data:JSON.stringify(data.field)  //转为json接收数据
            ,success:function(res){  //成功响应
                if(res.code==0) {  //0表示响应成功
                    layer.closeAll();
                    query(); //执行查询
                }else{
                    layer.alert(res.msg);
                }
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
}
/**
 * 通用删除方法
 * */
function myDelete(url) {
    $.ajax({
        url:url
        ,async:false
        ,type:'DELETE'
        ,success:function(res){  //成功响应
            if(res.code==0) {  //0表示响应成功
                query(); //执行查询
            }else{
                layer.alert(res.msg);
            }
        }
    });
}