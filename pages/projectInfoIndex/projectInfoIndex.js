// pages/projectInfoIndex/projectInfoIndex.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id : 0,
    status : 0,
    company : "",
    demand : 0,
    createTime : "",
    plan : 0,
    serviceTime : 0,
    nowLawyer : "",
    result : 0,
    modalHidden : true,
    advice : "",
    distributePlanId : 0,
    distributeServiceTime : "" ,
    modalHidden1 : true,
    number : "",
    note : ""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var item = JSON.parse(options.data);
    that.setData({
      id : item.id,
      status : options.status,
      company : item.company,
      demand : item.demand,
      createTime : app.formatDate(item.createTime),
      plan : item.plan,
      serviceTime : app.formatDate(item.startTime) + '-' + app.formatDate(item.endTime),
      nowLawyer : item.nowLawyer
    })
    if(this.data.status == "3"){
      wx.request({
        url: app.globalData.baseUrl + '/api/project/system/plan/' + that.data.id + '/closestRecord',
        method : 'GET',
        success : function(res){
          that.setData({
            distributePlanId : res.data.projectPlan.plan,
            distributeServiceTime : app.formatDate(res.data.projectPlan.startTime) + '-' + app.formatDate(res.data.projectPlan.endTime)
          })
        }
      })
    }else{
      if(this.data.status == "7"){
        wx.request({
          url: app.globalData.baseUrl + '/api/project/distributeLawyer/' + that.data.id + '/latestRecord',
          method : 'GET',
          success : function(res){
            if(res.data.code == 0){
              console.log(res.data)
              that.setData({
                lawyerId : res.data.entity.lawyer
              })
            }else{
              wx.showModal({
                title : "提示",
                content : "获取数据失败,请重试",
                showCancel: false
              })
            }

          }
        })
      }
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

  },
  showAdvice : function(event){
    this.setData({
      modalHidden : false,
      result : event.currentTarget.dataset.result
    })
  },
  inputAdvice : function(e){
    this.setData({
      advice : e.detail.value
    })
  },
  modalCandel : function(){
    this.setData({
      modalHidden : false,
      result : 0,
      advice : ""
    })
  },
  modalConfirm : function(){
    var that = this;
    if("" == this.data.advice){
      wx.showModal({
        title: "提示",
        content : "请输入审核意见",
        showCancel : false,
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + '/api/project/' + that.data.id + '/system/audit',
        method : 'POST',
        data:{
          result : this.data.result,
          advice : that.data.advice
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
              icon : 'fail',
              duration : 1500
            })
          }
        }
      })
    }
  },
  distributePlan : function(){
    wx.navigateTo({
      url: '../distributePlan/distributePlan?id=' + this.data.id,
    })
  },
  remindPay : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/api/project/' + that.data.id + '/remindPay',
      method : 'GET',
      success : function(res){
        if(res.data.code == 0){
          wx.showToast({
            title: '提醒成功',
            icon : 'success',
            duration : 1500
          })
        }else{
          wx.showToast({
            title: '提醒失败',
            icon : 'fail',
            duration : 1500
          })
        }
      }
    })
  },
  docProject : function(){
    this.setData({
      modalHidden1 : false
    })
  },
  inputNumber : function(e){
    this.setData({
      number : e.detail.value
    })
  },
  inputNote : function(e){
    this.setData({
      note : e.detail.value
    })
  },
  modalCandel1 : function(e){
    this.setData({
      modalHidden1 : true
    })
  },
  modalConfirm1: function(){
    var that = this;
    if("" == that.data.number){
      wx.showModal({
        title : "提示",
        content : "编号不能为空",
        showCancel : false
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + '/api/project/' + that.data.id + '/archive',
        method : 'POST',
        data : {
          archiveId : that.data.number,
          note : that.data.note
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
              icon : 'fail',
              duration : 1500
            })
          }
        }
      })
    }
  }
})