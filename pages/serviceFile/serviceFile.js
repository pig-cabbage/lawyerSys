// pages/serviceFile/serviceFile.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id : 0,
    result : [],
    root:[],
    value: [],
    detail : true,
    manager : "manager",

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
      that.setData({
        id : options.id
      })
      if(options.detail != null){
        if(options.detail == "false"){
          that.setData({
            manager : "company"
          })
        }
      }
      app.globalData.planId = options.id;

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
  initData: function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/api/service/file/template/' + this.data.id + '/list',
      method: 'GET',
      success: function(res){
        that.setData({
          value : res.data.list
        })
        // that.buildData();
      }
})
  },
  buildData : function(){
    var that = this;
    var temp = [];
    for(var  i = 0; i < that.data.result.length; i++){
      if(that.data.result[i].parent == -1){
        temp[temp.length] = that.data.result[i];
        if(that.data.result[i].type == 0){
        temp[temp.length - 1].children = that.diGui(that.data.result[i].id);
        }
      }
    }
    that.setData({
      value : temp
    })
  },
  diGui : function(parent){
    var that = this;
    var temp = [];
    for(var i = 0; i < that.data.result.length; i++){
      if(that.data.result[i].parent == parent){
        temp[temp.length] = that.data.result[i];
        if(that.data.result[i].type == 0){
        temp[temp.length - 1].children = that.diGui(that.data.result[i].id);
        }
      }
    }
    return temp;
  }
})