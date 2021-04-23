// pages/servicePlanIndex/servicePlanIndex.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    resultList:[],
    levelId:0,
    level: 0,
    chargeStandard:0,
    status: 1,
    key: "",
    sort:0,
    statusArray : ["离线", "在线"],
    timeArray : ["由近及远", "由远及近"],
    pickerStatusValue : 1,
    pickerTimeValue : 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      levelId: options.id,
      level: options.level,
      chargeStandard: options.chargeStandard
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
    this.initData()
  },
  initData: function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/service/plan/" + this.data.levelId + "/search",
      method: "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data:{
        status : this.data.status,
        key : this.data.key,
        sort : this.data.sort
      },
      success: function(res){
        if(res.data.code == 0){
        that.setData({
          resultList: res.data.list
        })
      }else{
        wx.showToast({
          title: '获取数据失败',
          icon: 'fail',
          complete: wx.navigateBack({
            delta: 1
           })
        })
      }
    }
    })
  },
  goToInfo: function(event){
    var that = this;
    let data = JSON.stringify(that.data.resultList[event.currentTarget.dataset.index]);
    wx.navigateTo({
      url: '../servicePlanInfo/servicePlanInfo?data=' + data + '&level=' +that.data.level + '&chargeStandard=' + that.data.chargeStandard,
    })
  },
  bindStatusPickerChange : function(e){
    this.setData({
      pickerStatusValue : e.detail.value,
      status : e.detail.value
    })
    this.determine();
  },
  bindTimePickerChange : function(e){
    this.setData({
      pickerTimeValue : e.detail.value,
      sort : e.detail.value
    })
    this.determine();
  },
  addServicePlan : function(){
    var that = this;
    wx.navigateTo({
      url: '../addServicePlan/addServicePlan?levelId=' + that.data.levelId + '&level=' +that.data.level + '&chargeStandard=' + that.data.chargeStandard,
    })
  },
  bindinput : function(e){
    this.setData({
      key : e.detail.value
    })
  },
  determine : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/service/plan/" + this.data.levelId + "/search",
      method: "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data:{
        status : this.data.status,
        key : this.data.key,
        sort : this.data.sort
      },
      success: function(res){
        if(res.data.code == 0){
        that.setData({
          resultList: res.data.list
        })
      }else{
        wx.showToast({
          title: '获取数据失败',
          icon: 'fail',
          complete: wx.navigateBack({
            delta: 1
           })
        })
      }
    }
    })
  }
})