var table = layui.table;
/**
 * 实例化渲染列表
 * 文档https://www.layui.com/doc/modules/table.html
 * */
var tableIns = table.render({
    elem: '#customerList'  //指向存放分页的容器，值可以是容器ID、DOM对象,当为id时不用加#
    ,url: '/customer/list' //数据接口
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
        /*{width: 80, type: 'checkbox',fixed:'left'}*/
        {field:  'customerId', title: '编号'}
        ,{field: 'realName', title: '真实姓名'}
        ,{field: 'sex', title: '性别' }
        ,{field: 'age', title: '年龄'}
        ,{field: 'phone', title: '手机号码'}
        ,{field: 'createTime', title: '创建时间'}
        ,{title: '操作', toolbar:'#barDemo'}   //绑定操作按钮.html文件中的表格id
    ]]
});

/**查询实现
 * 文档：https://www.layui.com/doc/modules/table.html
 * */
function query() {
    tableIns.reload({
        where: { //设定异步数据接口的额外参数，任意设
           realName: $("#realName").val()
            ,phone: $("#phone").val()
        }
        ,page: {
            curr: 1 //重新从第 1 页开始
        }
    });
}

/**
 * 新增客户
* */
function intoAdd(){
    openLayer('/customer/toAdd','新增客户'); /**响应controller层的映射*/
    layui.form.render();  /**执行渲染*/
    /**https://www.layui.com/doc/modules/form.html#render
     * 对应视频客户新增1-2:https://www.imooc.com/video/22873
     * */
    mySubmit('addSubmit','POST');
}



/**
 * 监听工具条*/
table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）
    let customerId = data.customerId;  //取得客户ID
    if(layEvent === 'detail'){ //查看
        openLayer('/customer/toDetail/'+customerId,'客户详情');
    } else if(layEvent === 'del'){ //删除
        layer.confirm('确定删除么？', function(index){
            layer.close(index);   //关闭确认框
            myDelete("/customer/"+customerId);
        });
    } else if(layEvent === 'edit'){ //编辑
        openLayer('/customer/toUpdate/'+customerId,'修改客户');
        layui.form.render();  /**执行渲染*/
        mySubmit('updateSubmit','PUT'); /**对应customerUpdate.html中的lay-filter的值updateSubmit*/
    }
});
