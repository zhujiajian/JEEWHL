var apiUrl = "http://127.0.0.1:8088/jee";
var jobUrl = "http://127.0.0.1:8088/jee";
var activityUrl = "http://127.0.0.1:8080/activity";
var oaUrl = "http://127.0.0.1:8088/jee";


/**
 * Created by whli on 2018/2/5.
 */
$(function () {
    var pathName=window.document.location.pathname;
    if(pathName.indexOf("login.html") == -1){
        var token = sessionStorage.getItem('token');
        if(!token){
            window.location.href= "../../page/login.html";
        }
    }
});

/**
 * 日志格式化
 * */
Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}