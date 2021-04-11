// pages/lawyerIndex/lawyerIndex.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentTab: 0,
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
  getContract : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/user/account/" + app.globalData.userInfo.id + "/chat",
      method : "GET",
      data : {
        role : 1
      },
      success : function(res){
        if(res.data.code == 0){
          that.setData({
            contractList : res.data.list,
          })
        }
      }
    })
  },
  goToChat : function(event){
    wx.navigateTo({
      url: '../chat/chat?other=' + event.currentTarget.dataset.man + "&session=" + event.currentTarget.dataset.id + "&sender=1",
    })
  },
  swichNav: function (e) {
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      switch(e.target.dataset.current){
        case 1:
          if(app.globalData.userInfo.certificationStatus <= 1){
            wx.showModal({
              title : "提示",
              content : "您还未认证。",
              showCancel : false,
            })
          }else{
          wx.redirectTo({
            url: '../lawyerWork/lawyerWork'
          })
        }
          break;
        case 2:
          wx.redirectTo({
            url: '../userLawyerInfo/userLawyerInfo',
          })
          break;
      }
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getContract();
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


})