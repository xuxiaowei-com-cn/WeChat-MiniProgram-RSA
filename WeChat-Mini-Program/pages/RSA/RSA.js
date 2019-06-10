// pages/RSA/RSA.js

import rsa from "../../js/jsencrypt.js"

Page({

  /**
   * 页面的初始数据
   */
  data: {
    publicKey: null
  },

  /**
   * form 发送数据
   */
  formSubmit: function(e) {

    var text = e.detail.value.text

    console.log('加密前的数据：', text)

    var publicKey = this.data.publicKey

    var encrypt = rsa.JSEncrypt

    encrypt.prototype.setPublicKey(publicKey);

    var encryptedText = encrypt.prototype.encrypt(text);

    console.log('加密后的数据：', encryptedText)

    wx.request({
      url: 'http://127.0.0.1/rsa/decrypt.do',
      data: {
        text: encryptedText
      },
      success: res => {
        // console.log("success", res)

        var resData = res.data
        var code = resData.code
        var msg = resData.msg
        var data = resData.data

        if (code == 0) {
          var text = data.text

          console.log(msg + "：", text)

        } else if (code == 1) {
          console.log("状态码错误", msg)
        } else {
          console.log("未知状态码：", code)
          console.log(msg)
        }

      },
      fail: res => {
        console.log("fail", res)
      }
    })

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

    /**
     * 获取公钥
     */
    wx.request({
      url: 'http://127.0.0.1/rsa/getPublicKey.do',
      success: res => {
        // console.log("success", res)

        var resData = res.data
        var code = resData.code
        var msg = resData.msg
        var data = resData.data

        if (code == 0) {
          var publicKey = data.publicKey

          console.log(msg, publicKey)

          this.setData({
            publicKey: publicKey
          })

        } else if (code == 1) {
          console.log("状态码错误", msg)
        } else {
          console.log("未知状态码：", code)
          console.log(msg)
        }

      },
      fail: res => {
        console.log("fail", res)
      }
    })

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})