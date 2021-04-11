// pages/distributePlan/distributePlan.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id : 0,
    modalHidden : true,
    levelList : [],
    planEntityList : [],
    planList : [],
    levelEntityList : [],
    pickerLevelValue : 0,
    pickerPlanValue : 0,
    planId : 0,
    startTime : "2010-1-1",
    endTime : "2010-1-1"
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var temp = [];
    var temp1 = app.globalData.levelList;
    for(var i = 0; i < app.globalData.levelList.length; i++){
      temp[temp.length] = app.globalData.levelList[i].level;
      temp1[i].children = [];
      temp1[i].childrenName = [];
    }
    this.setData({
      id : options.id,
      levelList : temp,
      levelEntityList : temp1
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
    if(this.data.levelList.length > 0){
    this.initData();
    }
  },
  choosePlan : function(){
    this.setData({
      modalHidden : false
    })
  },
  initData : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/service/plan/" + that.data.levelEntityList[0].id + "/search",
      method : 'GET',
      data : {
        status : 1,
        sort : "0"
      },
      success : function(res){
        if(res.data.code == 0){
          var str = 'that.data.levelEntityList[' + 0 + '].children'
          var str1 = 'that.data.levelEntityList[' + 0 + '].childrenName'
          var temp = []
          for(var i = 0; i< res.data.list.length; i ++){
            temp[temp.length] = res.data.list[i].name
          }
          that.setData({
            [str] : res.data.list,
            [str1] : temp,
            planList : temp,
            planEntityList : res.data.list
          })
         
          
        }else{
          wx.showModal({
            title : "提示",
            content : "获取数据失败，请重试",
            showCancel : false
          })
        }

      }
    })
  },
  bindLevelPickerChange : function(e){
    var that = this;
    this.setData({
      pickerLevelValue : e.detail.value,
    });
    if(that.data.levelEntityList[e.detail.value].children.length != 0){
      that.setData({
        planList : that.data.levelEntityList[e.detail.value].childrenName,
        planEntityList : that.data.levelEntityList[e.detail.value].children
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + "/api/service/plan/" + this.data.levelEntityList[e.detail.value].id + "/search",
        method : 'GET',
        data : {
          status : 1,
          sort : "0"
        },
        success : function(res){
          if(res.data.code == 0){
            var str = 'that.data.levelEntityList[' + e.detail.value + '].children'
            var str1 = 'that.data.levelEntityList[' + e.detail.value + '].childrenName'
            var temp = []
            for(var i = 0; i< res.data.list.length; i ++){
              temp[temp.length] = res.data.list[i].name
            }
            that.setData({
              [str] : res.data.list,
              [str1] : temp,
              planList : temp,
              planEntityList : res.data.list
            })
           
            
          }else{
            wx.showModal({
              title : "提示",
              content : "获取数据失败，请重试",
              showCancel : false
            })
          }
  
        }
      })
    }
  },
  bindPlanPickerChange : function(e){
    this.setData({
      pickerPlanValue : e.detail.value,
      planId : this.data.planEntityList[e.detail.value].id
    })
  },
  bindStartTimeChange :function(e){
    this.setData({
      startTime : e.detail.value
    })
  },
  bindEndTimeChange : function(e){
    this.setData({
      endTime : e.detail.value
    })
  },
  modalConfirm : function(){
    this.setData({
      modalHidden :true
    })
  },
  modalCandel : function(){
    this.setData({
      modalHidden :true
    })
  },
  distributePlan : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/api/project/' + that.data.id + '/system/distributePlan',
      method : 'POST',
      data:{
        plan : that.data.planId,
        startTime : that.data.startTime,
        endTime : that.data.endTime
      },
      success : function(res){
        if(res.data.code == 0){
          wx.showToast({
            title: '操作成功',
            icon : 'success',
            duration : 1500,
            complete : wx.navigateBack({
              delta: 2,
            })
          })
        }else{
          console.log(res.data.code)
          wx.showToast({
            title: '操作失败',
            icon : 'success',
            duration : 1500
          })
        }
      }
    })
  }


})