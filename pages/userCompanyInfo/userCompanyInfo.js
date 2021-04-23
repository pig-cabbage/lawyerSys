// pages/userCompanyInfo/userCompanyInfo.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentTab : 3,
    userInfo: {},
    items: [
      {
        "text": "消息",
        "iconPath": "/icon/message.png",
        "selectedIconPath": "/icon/message_select.png"
      },
      {
        "text": "发起 咨询",
        "iconPath": "/icon/add.png",
        "selectedIconPath": "/icon/add_select.png"
      },
      {
        "text": "历史咨询",
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

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (app.globalData.userInfoFront != -1) {
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
  swichNav: function (e) {
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      switch(e.target.dataset.current){
        case 0:
          wx.redirectTo({
            url: '../companyIndex/companyIndex'
          })
          break;
        case 1:
          if(app.globalData.userInfo.certificationStatus <= 1){
            wx.showModal({
              title : "提示",
              content : "您还未认证。",
              showCancel : false,
            })
          }else{
          wx.redirectTo({
            url: '../addProject/addProject'
          })
        }
          break;
        case 2:
          if(app.globalData.userInfo.certificationStatus <= 1){
            wx.showModal({
              title : "提示",
              content : "您还未认证。",
              showCancel : false,
            })
          }else{
          wx.redirectTo({
            url: '../projectHistory/projectHistory',
          })
        }
          break;
      }
    }
  },


  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
})