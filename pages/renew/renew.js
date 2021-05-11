// pages/renew/renew.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    modalHidden : true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      projectId : options.id
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
      url: app.globalData.baseUrl + "/api/project/distributePlan/" + that.data.projectId + '/closestRecord',
      method : 'GET',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success  : function(res){
        if(res.data.code == 0){
          that.setData({
            recordId : res.data.projectPlan.id,
            planId : res.data.projectPlan.plan,
            startTime : res.data.projectPlan.startTime,
            endTime : res.data.projectPlan.endTime,
            serviceTime :  app.formatDate(res.data.projectPlan.endTime) + "-" + app.formatDate(2 * new Date(res.data.projectPlan.endTime).getTime() - new Date(res.data.projectPlan.startTime).getTime()),
            cost : res.data.projectPlan.cost
          })
        }
      }
    })
  },
  pay : function(){
    this.setData({
      modalHidden : false
    })
  },
  modalCandel : function(){
    this.setData({
      modalHidden : true
    })
  },
  modalConfirm : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/" + that.data.projectId + "/company/renewal",
      method : 'POST',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data : {
        plan : that.data.planId,
        cost : that.data.cost,
        startTime : new Date(that.data.endTime),
        endTime : new Date(2 * new Date(that.data.endTime).getTime() - new Date(that.data.startTime).getTime())
      },
      success : function(res){
        if(res.data.code == 0){
          wx.showToast({
            title: '续费成功',
            icon : 'success',
            duration : 1500,
            complete : wx.navigateBack({
              delta: 2,
            })
          })
        }else{
          wx.showToast({
            title: '续费失败',
            icon : 'error',
            duration : 1500
          })
        }
      }
    })
  }
})