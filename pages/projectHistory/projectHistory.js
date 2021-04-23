// pages/projectHistory/projectHistory.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentTab : 2,
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
    ],

  },
  swichNav: function (e) {
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      switch(e.target.dataset.current){
        case 0:
          wx.redirectTo({
            url: '../companyIndex/companyIndex',
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
        case 3:
          wx.redirectTo({
            url: '../userCompanyInfo/userCompanyInfo',
          })
          break;
      }
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
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
    this.initData();
  },

  initData : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/company/" + app.globalData.userInfo.id + "/allList",
      method : "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success : function(res){
        if(res.data.code == 0){
          that.setData({
            projectList : res.data.list
          })
          for(var i = 0; i< that.data.projectList.length; i++){
            var createTime = "projectList[" + i + "].createTimeBrief";
            var status = "projectList[" + i + "].statusChina";
            that.setData({
              [createTime] : app.formatDate(that.data.projectList[i].createTime),
              [status] : app.globalData.projectStatus[that.data.projectList[i].status]
            })
          }
        }else{
          wx.showModal({
            title : "提示",
            content : "获取数据失败，请重试"
          })
        }
      }
    })
  },
  gotoDetail : function(event){
    var that = this;
    var data = JSON.stringify(that.data.projectList[event.currentTarget.dataset.index])
    wx.navigateTo({
      url: '../projectDetail/projectDetail?data=' + data,
    })
  }


})