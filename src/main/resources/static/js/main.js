/**
 * 打开选项卡，进入相应的主页
 * */
function showTab(url,name,id){
    let length = $("li[lay-id="+id+"]").length;
    let element = layui.element;
    /**根据id打开页面，已经打开的主界面不再重复显示*/
    if(length==0) {
        let fullUrl = "/" + url;   //带项目名的全路径
        /*iframe进行隔离，id重复也可多个页面在主界面显示，否则是个单页面*/
        let height = $(window).height()-185
        let content = '<iframe style="width: 100%;height: ' + height + 'px" src = "' + fullUrl + '" frameborder="0" scrolling="no">'
        element.tabAdd('menu', {
            title: name,
            content: content,
            id: id
        });
    }
    element.tabChange("menu",id);
}