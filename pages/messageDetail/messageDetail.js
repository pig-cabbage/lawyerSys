// pages/messageDetail/messageDetail.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  initData : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + that.data.requestUrl,
      method : "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success(res){
        if(res.data.code == 0){
          console.log(res.data)
        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      requestUrl : options.url
    })
    that.initData();
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