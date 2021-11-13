
var deskeyming;//des密码明文
var pwdKey;//rsa公钥

var apiurl = "http://localhost:9000";//接口base

var puburl = apiurl+"/getpublic?username=";//公钥接口
var deskeyurl = apiurl+"/putdes";//对称加密密钥上传接口



//获得非对称公钥
function getPubKey(flag,fun){
	$.get(puburl+flag,function(data,status){

    	pwdKey = new RSAUtils.getKeyPair(data.exponent,"",data.modulus);//得到公钥
    	console.log("rsa公钥");
    	console.log(pwdKey);
    	fun();
    	
	});
}


//生成对称加密密钥密文 且 制作对称加密密钥密文
function makeDes(flag,fun){

	deskeyming = generateMixed(16);//生成des密钥明文
	console.log("des密钥");
	console.log(deskeyming);

	var encrypedPwd = RSAUtils.encryptedString(pwdKey,deskeyming);//rsa加密des密钥

	var deskeymi = {keyCiphertext:encrypedPwd,username:flag};//组装json

	$.post(deskeyurl,deskeymi,function(data,status){//发送rsa密文到服务端（密文为des密钥的加密密文）
    	fun();
	});
}


//post加密请求
function myKeyPost(flag,para,url,fun){

	getPubKey(flag,function(){//首先获取rsa公钥
		makeDes(flag,function(){//生成并上传des密钥

			var desmi = encryptByDES(para,deskeyming);//生成发送数据的des密文
			$.post(url,{username:flag,content:desmi},function(data,status){//发送des密文（包含通讯数据）
	    	
	    		fun(decryptByDES(data.ret,deskeyming));//通讯回调
    	
  			});
		});
		

	});
	

	
}



//取des密码
var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
function generateMixed(n) {
     var res = "";
     for(var i = 0; i < n ; i ++) {
         var id = Math.ceil(Math.random()*35);
         res += chars[id];
     }
     return res;
}



//DES 加密 对称加密
function encryptByDES(message, key) {
	var keyHex = CryptoJS.enc.Utf8.parse(key);
	var encrypted = CryptoJS.DES.encrypt(message, keyHex, {
		mode: CryptoJS.mode.ECB,
		padding: CryptoJS.pad.Pkcs7
	});
	return encrypted.toString();
}

//DES 解密 对称解密
function decryptByDES(ciphertext, key) {
	var keyHex = CryptoJS.enc.Utf8.parse(key);
	// direct decrypt ciphertext
	var decrypted = CryptoJS.DES.decrypt({
		ciphertext: CryptoJS.enc.Base64.parse(ciphertext)
	}, keyHex, {
		mode: CryptoJS.mode.ECB,
		padding: CryptoJS.pad.Pkcs7
	});
	return decrypted.toString(CryptoJS.enc.Utf8);
}


//调用示例
// myKeyPost(generateMixed(20),"test","http://localhost/putdata",function(data){
// 	console.log("数据回环："+data);
// });