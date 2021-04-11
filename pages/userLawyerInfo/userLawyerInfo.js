// pages/userLawyerInfo/userLawyerInfo.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentTab : 2,
     //这里只做tab名和显示图标
     items: [
      {
        "text": "消息",
        "iconPath": "/icon/message.png",
        "selectedIconPath": "/icon/message_select.png"
      },
      {
        "text": "业务",
        "iconPath": "/icon/history.png",
        "selectedIconPath": "/icon/history_select.png"
      },
      {
        "text": "我的",
        "iconPath": "/icon/info.png",
        "selectedIconPath": "/icon/info_select.png"
      }
    ]
  },
  swichNav: function (e) {
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      switch(e.target.dataset.current){
        case 0:
          wx.redirectTo({
            url: '../lawyerIndex/lawyerIndex'
          });
          break;
        case 1:
          wx.redirectTo({
            url: '../lawyerWork/lawyerWork',
          })
          break;
      }
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (app.globalData.userInfoFront) {
      this.setData({
        userInfo: app.globalData.userInfoFront,
      })
    }else{
      wx.getSetting({
        success: res => {
          if (res.authSetting['scope.userInfo']) {
            // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
            wx.getUserInfo({
              success: res => {
                // 可以将 res 发送给后台解码出 unionId
                app.globalData.userInfoFront = res.userInfo
                this.setData({
                  userInfo : res.userInfo
                })
                // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
                // 所以此处加入 callback 以防止这种情况
                if (this.userInfoReadyCallback) {
                  this.userInfoReadyCallback(res)
                }
              }
            })
          }
        }
      })
    }
    this.setData({
      accountId : app.globalData.userInfo.id,
      certificationStatus : app.globalData.userInfo.certificationStatus
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})