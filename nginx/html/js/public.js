var baseUrl="http://127.0.0.1:23456";

var loginUrl=baseUrl+"/login";
var registerUrl=baseUrl+"/register";

var getUserList=baseUrl+"/getuserlist";

function mlog(msg){
    console.log(msg);
}

function mNav(url){
    window.location.href=url;
}

function setS(key,value){
    localStorage.setItem(key,value);
}

function getS(key){
    return localStorage.getItem(key);
}


layui.use('layer', function(){
    var layer = layui.layer;
}); 



