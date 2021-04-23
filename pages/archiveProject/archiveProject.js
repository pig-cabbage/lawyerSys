// pages/archiveProject/archiveProject.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    data : "",
    status : 0,
    number : "",
    note : ""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      data : options.data,
      status : options.status
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
    this.initData();
  },
  initData : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl +'/api/project/archive/' + JSON.parse(that.data.data).id + '/info',
      method : 'GET',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success : function(res){
        if(res.data.code == 0){
          that.setData({
            number : res.data.projectArchive.archiveId,
            note : res.data.projectArchive.note
          })
        }else{
          wx.showToast({
            title: '获取数据失败',
            icon : 'fail',
            duration : 1500,
            complete : wx.navigateBack({
              delta: 1,
            })
          })
        }
      }
    })
  }

})