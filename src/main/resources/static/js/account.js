layui.laydate.render({
      elem: '#createTimeRange'         /**id对应accountList.html中的创建时间id*/
      ,range:true   /**查询时间戳*/
});
var table = layui.table;
/**文档列表*/
var tableIns = table.render({
    id:"test"  /**对应accountList.html中的lay-filter="test"*/
    ,elem: '#accountList'  //指向存放分页的容器，值可以是容器ID、DOM对象,当为id时不用加#
    ,url: '/account/list' //数据接口
    ,page: true //开启分页
    ,limits: [5, 10, 20]  //分页条目
    ,parseData: function(res){ //res 即为原始返回的数据
        return {
            "code": res.code, //解析接口状态
            "msg": res.msg, //解析提示文本
            "count": res.data.count, //解析数据长度
            "data": res.data.records//解析数据列表
        };
    }
    ,cols: [[ //表头, 通过field将title显示子对应的列上
         {field: 'username', title: '用户名'}
        ,{field: 'realName', title: '真实姓名'}
        ,{field: 'roleName', title: '角色名称'}
        ,{field: 'sex', title: '性别' }
        ,{field: 'createTime', title: '创建时间'}
        ,{title: '操作', toolbar:'#barDemo'}   //绑定操作按钮
    ]]
});
/**查询实现
 * 文档：https://www.layui.com/doc/modules/table.html
 * */
function query() {  /**在accountList.html中onclick="query()"*/
    tableIns.reload({
        where: { //设定异步数据接口的额外参数，任意设
            realName: $("#realName").val()
            ,email: $("#email").val()
            ,createTimeRange:$("#createTimeRange").val()
        }
        ,page: {
            curr: 1 //重新从第 1 页开始
        }
    });
}
/**新增账号页*/
function intoAdd() {
    openLayer('/account/toAdd','新增账户'); /**响应controller层的映射*/
    layui.form.render();  /**执行渲染*/
    mySubmit('addSubmit','POST');
}

/**
 * 监听工具条*/
table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）
    let accountId = data.accountId;  //取得账户ID
    if(layEvent === 'detail'){ //查看
        openLayer('/account/toDetail/'+accountId,'账号详情');
    } else if(layEvent === 'del') { //删除
        layer.confirm('确定删除么？', function(index){
            layer.close(index);   //执行删除关闭
            myDelete("/account/"+accountId);
        });
    } else if(layEvent === 'edit'){ //编辑
        openLayer('/account/toUpdate/'+accountId,'修改账号');
        layui.form.render();  /**执行渲染*/
        mySubmit('updateSubmit','PUT'); /**对应accountUpdate.html中的lay-filter的值updateSubmit*/
    }
});
/**
 * 添加账号验证
 * */
layui.form.verify({
    checkUsername: function(value, item){ //value：表单的值、item：表单的DOM对象
        let error = null;  //账号添加失败的返回信息
        let url = '/account/'+value;  //value：用户名
        /**取accountUpdate.html中输入用户名的accountId*/
        let accountId = $("input[name='accountId']").val();
        /**已有信息提交*/
        if(typeof(accountId) != 'undefined'){
            url += '/'+accountId; //带原用户Id返回
        }
        $.ajax({
            url:url
            ,async:false
            ,type:'GET'
            ,success:function(res){  //成功响应
                if(res.code==0) {  //0表示响应成功
                    if(res.data>0) {
                        error = "用户名已存在";
                    }
                }else{
                    error = "用户名检测出错";
                }
            }
            ,error:function () {
                error = "用户名检测出错";  //其他未知错误
            }
        });
        if(error!=null) {
            return error;
        }
    }
});




