// pages/projectMessage/projectMessage.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  bottom: function () {
    var that = this;
    this.setData({
      scrollTop: 1000000,
      // chatHeight : 80
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      projectId : options.id,
      role : options.role
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
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/message/project/" + that.data.projectId +  "/role/" + that.data.role,
      method : "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success(res){
        if(res.data.code == 0){
          that.setData({
            list : res.data.list
          })
        }
      }
    })
  },
  toToDetail : function(e){
    var content = e.currentTarget.dataset.content;
    if(content == "律师信息"){
      wx.navigateTo({
        url: '../lawyerInfo/lawyerInfo?id=' + e.currentTarget.dataset.id,
      })
    }else{
      if(content == "方案信息"){
        wx.navigateTo({
          url: '../servicePlanInfo/servicePlanInfo?id=' + e.currentTarget.dataset.id,
        })
      }
    }
  },
  goToItem :function(e){
    var id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '../todoItemDetail/todoItemDetail?id=' + id,
    })
  }

})