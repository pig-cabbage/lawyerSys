// pages/companyDemand/companyDemand.js
const app = getApp();
Page({
  /**
   * 页面的初始数据
   */
  data: {
    id : 0,
    recommendPlan:0,
    demandPlan:0,
    content : "",
    objection : 0,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      id :options.id
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
  initData :function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/api/project/company/demand/info/' + this.data.id,
      method : 'GET',
      success : function(res){
        that.setData({
          recommendPlan : res.data.projectCompanyDemand.recommendPlan,
          demandPlan : res.data.projectCompanyDemand.demandPlan,
          content :res.data.projectCompanyDemand.content,
          objection : res.data.projectCompanyDemand.objection
        })
      }
    })
  }
})