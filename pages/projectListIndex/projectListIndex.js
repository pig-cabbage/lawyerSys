// pages/projectListIndex/projectList/index.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    statusList : [],
    resultList : [],
    key : "",
    fromLawyer : null,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if(options.fromLawyer != null){
      this.setData({
        fromLawyer : options.fromLawyer
      })
    }else{
      this.setData({
        statusList : JSON.parse(options.index)
      })
    }

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
    var that = this;
    if(that.data.fromLawyer != null){
      this.getLawyerProjectList();
    }else{
      this.getSystemProjectList();
    }
    
  },
  getLawyerProjectList : function(){
    var that = this;
    if(that.data.fromLawyer == "新分配"){
      wx.request({
        url: app.globalData.baseUrl + "/api/project/lawyer/" + app.globalData.userInfo.id + "/newList",
        method : "GET",
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
        success : function(res){
          if(res.data.code == 0){
            that.setData({
              resultList : res.data.list
            })
          }else{
            wx.showModal({
              title : "提示",
              content : "获取数据失败,请重试",
              showCancel : false
            })
          }
        }
      })
    }else{
      if(that.data.fromLawyer == "在办"){
        wx.request({
          url: app.globalData.baseUrl + "/api/project/lawyer/" + app.globalData.userInfo.id + "/nowList",
          method : "GET",
          header : {
            'cookie' : wx.getStorageSync("sessionid")
          },
          success : function(res){
            if(res.data.code == 0){
              that.setData({
                resultList : res.data.list
              })
            }else{
              wx.showModal({
                title : "提示",
                content : "获取数据失败,请重试",
                showCancel : false
              })
            }
          }
        }) 
      }else{
        wx.request({
          url: app.globalData.baseUrl + "/api/project/lawyer/" + app.globalData.userInfo.id + "/endList",
          method : "GET",
          header : {
            'cookie' : wx.getStorageSync("sessionid")
          },
          success : function(res){
            if(res.data.code == 0){
              that.setData({
                resultList : res.data.list
              })
            }else{
              wx.showModal({
                title : "提示",
                content : "获取数据失败,请重试",
                showCancel : false
              })
            }
          }
        }) 
      }
    }
  },
  getSystemProjectList : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/api/project/list',
      method : 'GET',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data:{
        status : that.data.statusList,
        key : that.data.key
      },
      success: function(res){
        that.setData({
          resultList : res.data.list
        })
      }
    })
  },
  bindinput:function(e){
    this.setData({
      key : e.detail.value
    })
  },
  determine : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/api/project/list',
      method : 'GET',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data:{
        status : that.data.statusList,
        key : that.data.key
      },
      success: function(res){
        that.setData({
          resultList : res.data.list
        })
      }
    })
  },
  goToInfo : function(event){
    var that = this;
    var data = JSON.stringify(this.data.resultList[event.currentTarget.dataset.index])
    if(that.data.fromLawyer == "已结束"){
      wx.navigateTo({
        url: '../workRecord/workRecord?data=' + data,
      })
    }else{
      if(that.data.fromLawyer == "在办"){
        wx.navigateTo({
          url: '../lawyerProjectDetail/lawyerProjectDetail?data=' + data,
        })
      }else{
        var statusId = this.data.statusList[0];
        switch(statusId){
          case 6:
            wx.navigateTo({
              url: '../projectInfoBefore/projectInfoBefore?data=' + data + '&status=' + statusId,
            })
            break;
          case 16:
            wx.navigateTo({
              url: '../projectInfoBefore/projectInfoBefore?data=' + data + '&status=' + statusId,
            })
            break;
          case 12:
            wx.navigateTo({
              url: '../projectInfoBefore/projectInfoBefore?data=' + data + '&status=' + statusId,
            })
            break;
          case 10:
              wx.navigateTo({
                url: '../projectLawyerComplaint/projectLawyerComplaint?data=' + data + '&status=' + statusId,
              })
              break;
          default:
            wx.navigateTo({
              url: '../projectInfoIndex/projectInfoIndex?data=' + data + '&status=' + this.data.resultList[event.currentTarget.dataset.index].status,
            })
            break;
      }
    }
   
    }
  }
})