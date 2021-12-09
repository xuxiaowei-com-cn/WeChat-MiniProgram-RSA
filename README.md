# 捐助与转载

- 整理不易，捐助随意。

- 尊重他人的劳动成果，转载请注明地址。

<p align=center>
  <a href="https://xuxiaowei.com.cn">
    <img src="https://cdn2.xuxiaowei.com.cn/img/QRCode.png/xuxiaowei.com.cn" alt="徐晓伟工作室" width="360">
  </a>
</p>

# WeChat-MiniProgram-RSA
微信小程序 RSA 非对称性加密

- JS 非对称性加密使用了 [jsencrypt](https://github.com/travist/jsencrypt) ，对 jsencrypt 进行了错误修正（符合微信小程序语法），调用方式也发生了少许变化，使用方法如下，详情参见示例 [RSA.js](https://github.com/XXWXHK/WeChat-MiniProgram-RSA/blob/master/WeChat-Mini-Program/pages/RSA/RSA.js)
  - 使用的版本为 jsencrypt-3.0.0-rc.1
  - jsencrypt-3.2.1
      - 需要将`webpack.config.js`文件中的`libraryTarget`修改为`assign`
      - 然后运行`build`进行打包
      - 修改`jsencrypt.min.js`文件
      - 在开头添加`var JSEncrypt={prototype: {}};`
      - 在`if(window.crypto&&window.crypto.getRandomValues)`前添加`var window={crypto:{}};`
      - 在最后添加`module.exports={JSEncrypt: JSEncrypt}`
      - 已修改完成的版本，参见：[jsencrypt.min-3.2.1.js](./WeChat-Mini-Program/js/jsencrypt.min-3.2.1.js)
      - 使用`jsencrypt.min.js`文件即可，使用方法同：[RSA.js](./WeChat-Mini-Program/pages/RSA/RSA.js)

```

// 1、在使用页面引入 /WeChat-Mini-Program/js/jsencrypt.js 并重命名：

import rsa from "../../js/jsencrypt.js"

// 2、使用别名进行创建 JSEncrypt（原始命令：var encrypt = new JSEncrypt()）

var encrypt = rsa.JSEncrypt

// 3、设置从后台获取的公钥（原始命令：encrypt.setPublicKey("从后台获取的公钥")），
// 参见：java/src/main/java/cn/com/xuxiaowei/util/rsa/RsaUtils.java ，
// 使用方式参见：/java/src/main/java/cn/com/xuxiaowei/handlerinterceptor/JsEncryptHandlerInterceptor.java

encrypt.prototype.setPublicKey("从后台获取的公钥");

// 4、加密数据（原始命令：var encrypted = encrypt.encrypt("需要加密的数据")）

var encrypted = encrypt.prototype.encrypt("需要加密的数据");

// 5、解密数据参见 java/src/main/java/cn/com/xuxiaowei/util/rsa/RsaUtils.java ，
// 使用方式参见：/java/src/main/java/cn/com/xuxiaowei/controller/RsaRestController.java


// 注：所有使用方法请从 JSEncrypt.prototype 中获取

```

- 发送数据使用了 [weapp-cookie](https://github.com/charleslo1/weapp-cookie) 进行保持 Cookie 机制

- 样式使用了 [weui-wxss](https://github.com/Tencent/weui-wxss)
