// pages/projectLawyerComplaint/projectLawyerComplaint.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    data : "",
    status : 0,
    changeLawyerApplyId : 0,
    createTime : "",
    reason : "",
    projectUserChangeLawyerString : "",
    result : 0,
    advice : "",
    complaintId : 0,
    modalHidden : true
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
      url: app.globalData.baseUrl + '/api/project/lawyerDealChangeApply/' + JSON.parse(that.data.data).id + '/info',
      method : 'GET',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success : function(res){
        if(res.data.code == 0){
          that.setData({
            complaintId : res.data.projectLawyerComplaint.id,
            createTime : app.formatDate(res.data.projectLawyerComplaint.createTime),
            reason : res.data.projectLawyerComplaint.reason,
            projectUserChangeLawyerString : JSON.stringify(res.data.projectUserChangeLawyer)
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
  },
  refuseLawyer : function(){
    this.setData({
      result : 0,
      modalHidden : false
    })
  },
  refuseCompany : function(){
    this.setData({
      result : 1,
      modalHidden : false
    })
  },
  inputAdvice : function (e){
    this.setData({
      advice : e.detail.value
    })
  },
  modalCandel : function(){
    this.setData({
      modalHidden :true
    })
  },
  modalConfirm : function() {
    var that = this;
    if(that.data.advice == ""){
      wx.showModal({
        title : '提示',
        content : '处理意见不能为空',
        showCancel : false
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + '/api/project/' + JSON.parse(that.data.data).id + '/system/dealComplaint',
        method : 'POST',
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
        data : {
          complaint : that.data.complaintId,
          result : that.data.result,
          advice : that.data.advice
        },
        success : function(res){
          if(res.data.code == 0){
            wx.showToast({
              title: '操作成功',
              icon : 'success',
              duration : 1500,
              complete : wx.navigateBack({
                delta: 1,
              })
            })
          }else{
            wx.showToast({
              title: '操作失败',
              icon : 'fail',
              duration : 1500
            })
          }
        }
      })
    }
  }

})