// pages/todoItemDetail/todoItemDetail.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    modalHidden : true,
    modalHidden1 : true,
    objectionContent : "",
    modalHidden2 : true,
    reason : "",
    modalHidden3 : true,
    reason1 : ""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    if(options.data != null){
      var item = JSON.parse(options.data);
      that.setData({
        id : item.item,
        project : item.project,
        content : item.itemContent,
        latestTime : app.formatDate(item.latestTime)
      })
      that.getMoreData(that.data.id)
    }else{
      wx.request({
        url: app.globalData.baseUrl + "/api/todoItem/info/" + options.id,
        method : "GET",
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
        success(res){
          if(res.data.code == 0){
            var item = res.data.projectUserTodoItem
            that.setData({
              id : item.item,
              project : item.project,
              content : item.itemContent,
              latestTime : app.formatDate(item.latestTime)
            })
            that.getMoreData(item.item)
          }else{
            wx.showModal({
              title : "提示",
              content : "获取数据失败，请重试",
              showCancel : false,
              success(res){
                wx.navigateBack({
                  delta: 1,
                })
              }
            })
          }
        }
      })
    }
  },
  getMoreData : function(id){
    var that = this;
    switch(id){
      case 1 :
        that.getPlan();
        that.setData({
        warn : "请在" + that.data.latestTime + "前完成支付，否则系统将终止此次流程！"
         });
        break;
      case 8 :
        that.setData({
          warn : "请在" + that.data.latestTime + "前完成操作，否则系统将默认执行拒绝操作！"
        })
        break;
      case 3 :
        that.getProject();
        that.getRecord();
        that.setData({
          warn : "请在" + that.data.latestTime + "前完成操作，否则系统将默认执行拒绝操作！"
         })
        break;
      case 4 :
        that.getApply();
        that.setData({
            warn : "请在" + that.data.latestTime + "前完成操作，否则系统将自动同意申请！"
          })
        break;
      default:
        that.setData({
          warn : "请在" + that.data.latestTime + "前完成操作，否则系统将自动为您指定律师！"
        })
        break;
    }
  },
  undertake : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/" + that.data.project + "/lawyer/determineUnderTake",
      method : "POST",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data : {
        lawyer : that.data.distributeId,
        carry : 1
      },
      success : function(res){
        if(res.data.code == 0){
          wx.showToast({
            title: '承接成功',
            icon : "success",
            duration : 1500,
            complete : wx.navigateTo({
              url: '../lawyerWork/lawyerWork',
            })
          })
        }else{
          console.log(res)
          wx.showModal({
            title : "提示",
            content : "操作失败，请重试",
            showCancel : false,
          })
        }
      }
    })
  },
  modalConfirm3 : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/" + that.data.project + "/lawyer/determineUnderTake",
      method : "POST",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data : {
        lawyer : that.data.distributeId,
        carry : 0,
        reason : that.data.reason1
      },
      success : function(res){
        if(res.data.code == 0){
          wx.showToast({
            title: '操作成功',
            icon : "success",
            duration : 1500,
            complete : wx.navigateBack({
              delta: 1,
            })
          })
        }else{
          wx.showModal({
            title : "提示",
            content : "操作失败，请重试",
            showCancel : false,
          })
        }
      }
    })
  },
  refuseUndertake : function(){
    this.setData({
      modalHidden3 : false
    })
  },
  modalCandel3 : function(){
    this.setData({
      modalHidden3 : true
    })
  },
  inputReason1 : function(e){
    this.setData({
      reason1 : e.detail.value
    })
  },
  getRecord : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/distributeLawyer/" + that.data.project +  "/latestRecord",
      method : "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success : function(res){
        if(res.data.code == 0){
          that.setData({
            distributeId : res.data.entity.id
          })
        }else{
          console.log(res)
          wx.showModal({
            title : "提示",
            content: "获取数据失败，请重试",
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
  getProject : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/info/" + that.data.project,
      method : "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success : function(res){
        if(res.data.code == 0){
          that.setData({
            companyId : res.data.projectBase.company,
            demand : res.data.projectBase.demand,
            planId : res.data.projectBase.plan,
            serviceTime : app.formatDate(res.data.projectBase.startTime) + "-" + app.formatDate(res.data.projectBase.endTime)
          })
        }else{
          wx.showModal({
            title : "提示",
            content : "获取数据失败，请重试。",
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
  getPlan : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/distributePlan/" + that.data.project + '/closestRecord',
      method : 'GET',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success  : function(res){
        console.log(res)
        if(res.data.code == 0){
          that.setData({
            recordId : res.data.projectPlan.id,
            planId : res.data.projectPlan.plan,
            serviceTime : app.formatDate(res.data.projectPlan.startTime) + '-' + app.formatDate(res.data.projectPlan.endTime),
            cost : res.data.projectPlan.cost
          })
        }
      }
    })
  },
  getApply : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/changeLawyer/" + that.data.project + '/latestInfo',
      method : 'GET',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success  : function(res){
        console.log(res)
        if(res.data.code == 0){
          that.setData({
            applyId : res.data.projectUserChangeLawyer.id,
            reason : res.data.projectUserChangeLawyer.reason
          })
        }
      }
    })
  },
  agreeApply : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/" + that.data.project + "/lawyer/dealChangeLawyer",
      method : "POST",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data : {
        changeLawyer : that.data.applyId,
        result : 0
      },
      success : function(res){
        if(res.data.code == 0){
          wx.showToast({
            title: '处理成功',
            icon : "sueecss",
            complete : wx.navigateBack({
              delta: 1,
            })
          })
        }else{
          console.log(res)
          wx.showModal({
            title : "提示",
            content : "操作失败，请重试",
            showCancel : false
          })
        }
      }
    })
  },
  refuseApply : function(){
    this.setData({
      modalHidden2 : false
    })
  },
  modalCandel2 : function(){
    this.setData({
      modalHidden2 : true
    })
  },
  inputReason : function(e){
    this.setData({
      reason : e.detail.value
    })
  },
  modalConfirm2 : function(){
    var that = this;
    if(that.data.reason == ""){
      wx.showModal({
        title : "提示",
        content : "申诉理由不能为空",
        showCancel : false
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + "/api/project/" + that.data.project + "/lawyer/dealChangeLawyer",
        method : "POST",
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
        data : {
          changeLawyer : that.data.applyId,
          result : 1,
          reason : that.data.reason
        },
        success : function(res){
          if(res.data.code == 0){
            wx.showToast({
              title: '处理成功',
              icon : "sueecss",
              complete : wx.navigateBack({
                delta: 1,
              })
            })
          }else{
            console.log(res)
            wx.showModal({
              title : "提示",
              content : "操作失败，请重试",
              showCancel : false
            })
          }
        }
      })
    }
  },
  pay : function(){
    this.setData({
      modalHidden : false
    })
  },
  modalCandel : function(){
    this.setData({
      modalHidden : true
    })
  },
  modalConfirm : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/api/project/' +  that.data.project + '/company/pay',
      method : 'GET',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success : function(res){
        if(res.data.code == 0){
          wx.showToast({
            title: '支付成功',
            icon : 'success',
            duration : 1500,
            complete : wx.navigateBack({
              delta: 1,
            })
          })
        }else{
          wx.showToast({
            title: '支付失败',
            icon : 'error',
            duration : 1500
          })
        }
      }
    })
  },
  objection : function(){
    this.setData({
      modalHidden1 : false
    })
  },
  modalCandel1 : function(){
    this.setData({
      modalHidden1 : true
    })
  },
  inputObjection : function(e){
    this.setData({
      objectionContent : e.detail.value
    })
  },
  modalConfirm1 : function(){
    var that = this;
    if(that.data.objectionContent == ""){
      wx.showModal({
        title : "提示",
        content : "异议内容不能为空",
        showCancel : false
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + '/api/project/' + that.data.project + '/comapny/objection',
        method : 'POST',
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
        data : {
          distributeRecord : that.data.planId,
          content : that.data.objectionContent
        },
        success : function(res){
          if(res.data.code == 0){
            wx.showToast({
              title: '操作成功',
              icon : 'success',
              duration : 1500,
              complete : wx.navigateBack({
                delta: 1,
              })
            })
          }else{
            wx.showToast({
              title: '操作失败',
              icon : 'error',
              duration : 1500
            })
          }
        }
      })
    }
  },
  chooseLawyer : function(){
    wx.navigateTo({
      url: "../chooseLawyer/chooseLawyer?id=" + this.data.project,
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
  renew : function(){
    wx.navigateTo({
      url: '../renew/renew?id=' + this.data.project,
    })
  },
  noRenew : function(){
    wx.navigateTo({
      url: '../evaluate/evaluate?id=' + this.data.project,
    })
  }

})