// pages/serviceManagerIndex/serviceManagerIndex.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
      levelList: [],
      modalHidden: true,
      array: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      index: 0,
      level: 0,
      chargeStandard: 0
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.initData();
  },
  initData: function(){
    this.setData({
      levelList : app.globalData.levelList
    })
  },
  addLevel: function(){
    var temp = [];
    for(var i = 0; i < this.data.array.length; i++){
      var tag = true;
      for(var j = 0; j < app.globalData.onlyLevelList.length; j++){
        if(this.data.array[i] == app.globalData.onlyLevelList[j]){
          tag = false;
          break;
        }
      }
      if(tag){
        temp[temp.length] = this.data.array[i];
      }
    }
    this.setData({
      modalHidden: false,
      array : temp
    })
  },
  bindPickerChange: function(e){
    this.setData({
      index: e.detail.value,
      level: this.data.array[e.detail.value]
    })
  },
  inputStandard: function(e){
    this.setData({
      chargeStandard: e.detail.value
    })
  },
  modalCandel: function(){
    this.setData({
      modalHidden: true
    })
    this.initData()
  },
  modalConfirm: function(e){
    var that = this
    wx.request({
      url: app.globalData.baseUrl + "/api/service/level/add",
      method: "POST",
      data:{
        level: this.data.level,
        chargeStandard: this.data.chargeStandard
      },
      success: function(res) {
        if(res.data.code == 0){
          wx.showToast({
            title: '新建成功',
            icon: 'success',
            duration: 1500,
          })
          app.getLevelList();
          that.setData({
            getLevelList : true
          })
          // wx.request({
          //   url: app.globalData.baseUrl + "/api/service/level/list",
          //   method: "GET",
          //   success: function(res){
          //     that.setData({
          //       levelList : res.data.page.list,
          //       modalHidden : true
          //     })
          //     app.globalData.levelList = res.data.page.list
          //   }
          // })
        }
      }
    })
  },
})