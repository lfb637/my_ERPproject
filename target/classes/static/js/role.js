var table = layui.table;
/**文档列表*/
var tableIns = table.render({
    id:"test"  /**对应roleList.html中的lay-filter="test"*/
    ,elem: '#roleList'  //指向存放分页的容器，值可以是容器ID、DOM对象,当为id时不用加#
    ,url: '/role/list' //数据接口
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
        {field: 'roleName', title: '角色名称'}
        ,{field: 'createTime', title: '创建时间'}
        ,{title: '操作', toolbar:'#barDemo'}   //绑定操作按钮
    ]]
});
/**查询实现
 * 文档：https://www.layui.com/doc/modules/table.html
 * */
function query() {
    tableIns.reload({
        where: { //设定异步数据接口的额外参数，任意设
            roleName: $("#roleName").val()
        }
        ,page: {
            curr: 1 //重新从第 1 页开始
        }
    });
}
/**新增角色页*/
function intoAdd() {
    openLayer('/role/toAdd','新增角色'); /**响应controller层的映射*/
    showTree('/role/listResource','resource');
    mySubmit('addSubmit', "POST", addIds);
}
/**
* 资源树
 * 监听工具条中当showCheckbox没有定义时showCheckbox为true，此时为编辑
 * 否则为false，则为查看
* */
function showTree(url,id,showCheckbox) {
    if(typeof (showCheckbox) == "undefined") {
        showCheckbox = true;
    }
    $.ajax({
        url:url
        ,async:false
        ,type:'GET'
        ,success:function(res){  //成功响应
            if(res.code==0) {  //0表示响应成功
                layui.tree.render({ /**执行渲染*/
                    elem: '#'+id  /**roleAdd.html中的资源id*/
                    ,data: res.data //数据源
                    ,id: id //定义索引
                    ,showCheckbox: showCheckbox
                });
            }
        }
    });
}
/**
 * 选中的资源Id
 * */
var addIds = function (field){   //通过field添加数据
   let checkedData = layui.tree.getChecked('resource');
   field.resourceIds = getIds(checkedData, []);
}

/**
 * 得到选中的ID并封装成数组
 * */
function getIds(checkedData,arr) {
    for (let i in checkedData) {
        arr.push(checkedData[i].id);
        //递归遍历当前菜单下的所有子菜单id
        arr = getIds(checkedData[i].children, arr);
    }
    return arr;
}

/**
 * 监听工具条*/
table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）
    let roleId = data.roleId;  //取得客户ID
    if(layEvent === 'detail'){ //查看
        openLayer('/role/toDetail/'+roleId,'角色详情');
        showTree('/role/listResource/'+roleId+'/1','resource',false);
    } else if(layEvent === 'del') { //删除
        layer.confirm('确定删除么？', function(index){
            layer.close(index);   //关闭确认框
            myDelete("/role/"+roleId);
        });
    } else if(layEvent === 'edit') { //编辑
        openLayer('/role/toUpdate/'+roleId,'修改客户');
        showTree('/role/listResource/'+roleId+'/0','resource');
        mySubmit('updateSubmit',"PUT",addIds); /**对应roleUpdate.html中的lay-filter的值updateSubmit*/
    }
});
