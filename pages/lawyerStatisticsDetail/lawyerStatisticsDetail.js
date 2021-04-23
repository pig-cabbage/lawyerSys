// pages/lawyerStatisticsDetail/lawyerStatisticsDetail.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    startDate: '2010-09',
    endDate : "2022-09",
  },
  bindDateChange1 : function(e){
    this.setData({
      startDate : e.detail.value
    })
    this.getData();
  },
  bindDateChange2 : function(e){
    this.setData({
      endDate : e.detail.value
    })
    this.getData();
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      id : options.id,
      startDate : options.startDate,
      endDate : options.endDate
    })
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.getData();
  },
  getData : function(){
    var that = this;
    wx.request({
    url: app.globalData.baseUrl + "/api/dataStatistics/lawyer/"+ that.data.id+  "/detail",
    method : "GET",
    header : {
      'cookie' : wx.getStorageSync("sessionid")
    },
    date : {
      startDate : that.data.startDate,
      endDate : that.data.endDate
    },
    success(res){
      if(res.data.code == 0){
        that.setData({
          list : res.data.LawyerMathDetail
        })
      }else{
        wx.showModal({
          title : "提示",
          content : "获取数据失败， 请重试",
          showCancel : false
        })
      }
    }
  })
  }
})