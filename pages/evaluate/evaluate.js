// pages/evaluate/evaluate.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    content : "为了给您提供更优质的服务，请您对律师的服务做出评价，谢谢配合！",
    advice : "",
    light:[],
    dark : [1,1,1,1,1]
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

  },
  light : function(event){
    var that = this;
    var num = event.currentTarget.dataset.index + 1;
    var temp = [];
    var temp1 = [];
    for(var i = 0; i < num; i++){
      temp[temp.length] = 1;
    }
    for(var i = 0; i < 5 - num; i ++){
      temp1[temp1.length] = 1;
    }
    that.setData({
      light : temp,
      dark : temp1
    })
  },
  dark : function(event){
    var that = this;
    var num = event.currentTarget.dataset.index + 1;
    var temp = that.data.light;
    var temp1 = [];
    for(var i = 0; i < num; i++){
      temp[temp.length] = 1;
    }
    for(var i = 0; i < 5 - that.data.light.length; i ++){
      temp1[temp1.length] = 1;
    }
    this.setData({
      light : temp,
      dark : temp1
    })
  },
  inputContent : function(e){
    this.setData({
      advice : e.detail.value
    })
  },
  evaluate : function(){
    var that = this;
    if(that.data.advice == ""){
      wx.showModal({
        title : "提示",
        content : "评价不能为空",
        showCancel : false
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + "/api/project/" + that.data.projectId + "/evaluation",
        method : "POST",
        data : {
          score : that.data.light.length,
          content : that.data.advice
        },
        success : function(res){
          if(res.data.code == 0){
            wx.showToast({
              title: '评价成功',
              icon : 'success',
              duration : 1500,
              complete : wx.navigateBack({
                delta: 2,
              })
            })
          }else{
            wx.showToast({
              title: '评价失败',
              icon : 'error',
              duration : 1500
            })
          }
        }
      })
    }
  }
})