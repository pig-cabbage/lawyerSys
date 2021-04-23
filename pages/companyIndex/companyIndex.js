// pages/companyIndex/companyIndex.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentTab: 0,
    count : 0,
    //这里只做tab名和显示图标
    contractList : [],
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
  goToMessage : function(event){
    wx.navigateTo({
      url: '../systemMessage/systemMessage',
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
            url: '../addProject/addProject?currentTab=1'
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
  onShow: function(){
    var that = this;
    wx.onSocketMessage(onMessage => {
      console.log('服务器返回的消息Index: ', onMessage.data)
      app.globalData.newestMessage = onMessage.data
      var item = JSON.parse(onMessage.data);
      var temp = that.data.contractList;
      for(var i = 0; i < temp.length; i++){
        if(temp[i].id == item.sessionId){
          temp[i].newestMessage = item;
          break;
        }
      }
      that.setData({
        contractList : temp
      })
      
    })
    this.getContractAndMessage();
  },
  getContractAndMessage : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/user/account/" + app.globalData.userInfo.id + "/chat",
      method : "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data : {
        role : 0
      },
      success : function(res){
        if(res.data.code == 0){
          that.setData({
            contractList : res.data.list,
            count : res.data.count
          })
        }
      }
    })
  },
  goToItem : function(event){
    wx.navigateTo({
      url: '../companyTodoItem/companyTodoItem',
    })
  },
  goToChat : function(event){
    wx.navigateTo({
      url: '../chat/chat?other=' + event.currentTarget.dataset.man + "&session=" + event.currentTarget.dataset.id + "&sender=0",
    })
  },

})