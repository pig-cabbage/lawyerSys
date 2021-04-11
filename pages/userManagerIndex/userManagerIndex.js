const app = getApp();
Page({
  data: {
    tabs: [],
    activeTab: 0,
    currentIndexNav: 0,
    result:[],
    key:'' 
  },
onLoad(){
  this.initResult()
},
  onShow() {
    const tabs = [
      {
        title: '企业用户'
      },
      {
        title: '律师用户'
      }
    ]
    
    this.setData({ tabs })
  },
  activeNav: function(e){
    var that = this;
    if(that.data.currentIndexNav != e.currentTarget.dataset.index){
    that.setData({
      currentIndexNav: e.currentTarget.dataset.index
    })
    if(that.data.currentIndexNav == 0){
      wx.request({
        url: app.globalData.baseUrl + "/api/user/company/search",
        method: "GET",
        data:{
          key: that.data.key
        },
        success: function(res){
          that.setData({
            result: res.data.page.list
          })
        },
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + "/api/user/lawyer/search",
        method: "GET",
        data:{
          key: that.data.key
        },
        success: function(res){
          that.setData({
            result: res.data.page.list
          })
        },
      })
    }
  }
  },
  initResult: function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/user/company/search",
      method: "GET",
      data:{
        key: that.data.key
      },
      success: function(res){
        that.setData({
          result: res.data.page.list
        })
      },
    })
  },
  determine: function (value) {
    var that = this
    if(that.data.currentIndexNav == 0){
      wx.request({
        url: app.globalData.baseUrl + "/api/user/company/search",
        method: "GET",
        data:{
          key: that.data.key
        },
        success: function(res){
          that.setData({
            result: res.data.page.list
          })
        },
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + "/api/user/lawyer/search",
        method: "GET",
        data:{
          key: that.data.key
        },
        success: function(res){
          that.setData({
            result: res.data.page.list
          })
        },
      })
    }
},
  selectResult: function (e) {
    
},
 
  bindinput:function(value){
    this.setData({
      key: value.detail.value
    })
  },
  goToInfo: function(event){
    var that = this;
    let data = JSON.stringify(that.data.result[event.currentTarget.dataset.index]);
    if(that.data.currentIndexNav == 0){
      wx.navigateTo({
        url: '../companyInfo/companyInfo?data=' + data,
      })
    }else{
      wx.navigateTo({
        url: '../lawyerInfo/lawyerInfo?data=' + data,
      })
    }
  },
  goToApplyIndex: function(){
    wx.navigateTo({
      url: '../authApplyIndex/authApplyIndex',
    })
  }
})