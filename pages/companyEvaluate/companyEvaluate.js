// pages/companyEvaluate/companyEvaluate.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    projectId : 0,
    score : 0,
    content : "",
    createTime : "",
    array1: [],
    array2: []
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
      url: app.globalData.baseUrl + '/api/project/companyEvaluate/' + that.data.projectId + '/info',
      method : 'GET',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success : function(res){
        if(res.data.code == 0){
          that.setData({
            score : res.data.projectCompanyEvaluation.score,
            content : res.data.projectCompanyEvaluation.content,
            createTime : res.data.projectCompanyEvaluation.createTime
          })
          var temp1 = [];
          var temp2 = [];
          for(var i = 0; i < that.data.score; i++){
            temp1[temp1.length]=1;
          }
          for(var j = 0; j < 5 - that.data.score; j++){
            temp2[temp2.length] = 1;
          }
          that.setData({
            array1 : temp1,
            array2 : temp2
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