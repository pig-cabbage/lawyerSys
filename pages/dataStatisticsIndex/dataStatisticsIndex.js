// pages/dataStatisticsIndex/dataStatisticsIndex.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    startDate: '2010-09',
    endDate : "2022-09",
    currentIndexNav : 0,
    serviceList : [],
    companyList : [],
    lawyerList : []

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  getServiceData : function(){
    var that  =  this;
    wx.request({
      url: app.globalData.baseUrl + "/api/dataStatistics/service",
      method : "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data : {
        startDate : that.data.startDate,
        endDate : that.data.endDate
      },
      success : function(res){
        if(res.data.code == 0){
          var item = res.data.ServiceMath
         that.setData({
           totalNumber : item.totalNumber,
           totalDate : item.totalDate,
           totalCost : item.totalCost,
           serviceList : item.list
         })
        }else{
          wx.showModal({
            title : "提示",
            content : "获取数据失败，请重试",
            showCancel : false,
            success(res){
              if(res.confirm){
                wx.navigateBack({
                  delta: 1,
                })
              }
            }

          })
        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    const tabs = [
      {
        title: '服务'
      },
      {
        title: '企业'
      },
      {
        title: '律师'
      }
    ]
    
    this.setData({ tabs })
    this.getData();
  },
  activeNav: function(e){
    var that = this;
    if(that.data.currentIndexNav != e.currentTarget.dataset.index){
    that.setData({
      currentIndexNav: e.currentTarget.dataset.index
    })
    if(that.data.currentIndexNav == 0 && that.data.serviceList.length == 0){
      that.initData();
    }else{
      if(that.data.currentIndexNav == 1 && that.data.companyList.length == 0){
        that.getCompanyData();
      }else{
        if(that.data.currentIndexNav == 2 && that.data.lawyerList.length == 0){
          that.getLawyerData();
        }
      }
    }
  }
},
getLawyerData : function(){
  var that = this;
  wx.request({
    url: app.globalData.baseUrl + "/api/dataStatistics/lawyer",
    method : "GET",
    header : {
      'cookie' : wx.getStorageSync("sessionid")
    },
    data : {
      startDate : that.data.startDate,
      endDate : that.data.endDate
    },
    success: function(res){
      if(res.data.code == 0){
        that.setData({
          lawyerList : res.data.LawyerMath
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
getCompanyData : function(){
  var that = this;
  wx.request({
    url: app.globalData.baseUrl + "/api/dataStatistics/company",
    method : "GET",
    header : {
      'cookie' : wx.getStorageSync("sessionid")
    },
    data : {
      startDate : that.data.startDate,
      endDate : that.data.endDate
    },
    success : function(res){
      if(res.data.code == 0){
        console.log(res)
        that.setData({
          companyList : res.data.CompanyMath
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
bindDateChange1 : function(e){
  this.setData({
    startDate : e.detail.value
  })
  this.getData();
},
bindDateChange2 : function(e){
  this.setData({
    endDate : e.detail.value
  })
  this.getData();
},
getData : function(){
  var that = this;
  switch(that.data.currentIndexNav){
    case 0: 
      that.getServiceData();
      that.setData({
        companyList : [],
        lawyerList : [],
      })
      break;
    case 1:
      that.getCompanyData();
      that.setData({
        serviceList : [],
        lawyerList : [],
      })
      break;
    case 2:
      that.getLawyerData();
      that.setData({
        serviceList : [],
        companyList : [],
      })
      break;
  }
},
goToDetail : function(e){
  var that = this;
  wx.navigateTo({
    url: '../lawyerStatisticsDetail/lawyerStatisticsDetail?id=' + e.currentTarget.dataset.id + "&startDate=" + that.data.startDate + "&endDate=" + that.data.endDate,
  })
  
},

})