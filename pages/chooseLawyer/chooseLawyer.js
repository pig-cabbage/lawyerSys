// pages/chooseLawyer/chooseLawyer.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    activeTab: 0,
    currentIndexNav: 0,
    lawyerList : [],
    key : "",
    projectId : "",
    level : -1,
    lawyerId : -1
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
    const tabs = [
      {
        title: '系统推荐'
      },
      {
        title: '自主选择'
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
    if(that.data.currentIndexNav == 1){
      if(that.data.level == -1){
        that.getLevelAndLevelList();
      }else{
      if(that.data.lawyerList.length == 0){
        wx.request({
          url: app.globalData.baseUrl + "/api/user/lawyer/search",
          method: "GET",
          header : {
            'cookie' : wx.getStorageSync("sessionid")
          },
          data:{
            key: that.data.key,
            level : that.data.level
          },
          success: function(res){
            that.setData({
              lawyerList: res.data.page.list
            })
          },
        })
      }
    }
    }
  }},
  goToInfo: function(event){
    var that = this;
    let data = JSON.stringify(that.data.lawyerList[event.currentTarget.dataset.index]);
      wx.navigateTo({
        url: '../lawyerInfo/lawyerInfo?data=' + data,
      }) 
  },
  bindinput : function(e){
    var that = this;
    that.setData({
      key : e.detail.value
    })
  },
  determine : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/user/lawyer/search",
      method: "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data:{
        key: that.data.key,
        level : that.data.level
      },
      success: function(res){
        that.setData({
          lawyerList: res.data.page.list
        })
      },
    })
  },
  getLevelAndLevelList : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/" + that.data.projectId + "/level",
      method : "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success : function(res){
        if(res.data.code == 0){
          that.setData({
            level : res.data.level
          })
          wx.request({
            url: app.globalData.baseUrl + "/api/user/lawyer/search",
            method: "GET",
            header : {
              'cookie' : wx.getStorageSync("sessionid")
            },
            data:{
              key: that.data.key,
              level : res.data.level
            },
            success: function(res){
              console.log(res.data)
              that.setData({
                lawyerList: res.data.page.list
              })
            },
          })
        }
      }
    })
  },
  radioChange: function (e) {
    var radioItems = this.data.lawyerList;
    for (var i = 0, len = radioItems.length; i < len; ++i) {
        radioItems[i].checked = radioItems[i].id == e.detail.value;
    }
    this.setData({
        lawyerId : e.detail.account,
        lawyerList: radioItems,
        [`formData.radio`]: e.detail.value
    });
},
  chooseLawyer : function(){
    var that = this;
    if(that.data.currentIndexNav == 0){
      wx.request({
        url: app.globalData.baseUrl +  "/api/project/" + that.data.projectId + "/company/chooseLawyer",
        method : "POST",
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
        data : {
          recommendLawyer : 1
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
            wx.showToast({
              title: '操作失败，请重试',
              icon : 'error',
              duration : 1500
            })
          }
        }
      })
    }else{
      if(that.data.lawyerId == -1){
        wx.showModal({
          title : "提示",
          content : "请选择律师",
          showCancel : false
        })
      }else{
        wx.request({
          url: app.globalData.baseUrl +  "/api/project/" + that.data.projectId + "/chooseLawyer",
          method : "POST",
          header : {
            'cookie' : wx.getStorageSync("sessionid")
          },
          data : {
            recommendLawyer : 0,
            demandLawyer : that.data.lawyerId
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
              wx.showToast({
                title: '操作失败，请重试',
                icon : 'error',
                duration : 1500
              })
            }
          }
        })
      }
    }
    
  }
})