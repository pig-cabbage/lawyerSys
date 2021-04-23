// pages/systemMessage/systemMessage.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  goToDetail : function(e){
    wx.navigateTo({
      url: '../todoItemDetail/todoItemDetail?id=' + e.currentTarget.dataset.id,
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      this.getMessage();
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
  },
  getToDetail : function(e){
    wx.navigateTo({
      url: '../projectInfoIndex/projectInfoIndex?id=' + e.currentTarget.dataset.id
    })
  },
  getMessage : function() {
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/message/system/user/" + app.globalData.userInfo.id,
      method : "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success(res){
        if(res.data.code == 0){
          that.setData({
            allContentList : res.data.list
          })
        }
      }
    })
  }
})