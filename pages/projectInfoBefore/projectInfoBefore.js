// pages/servicePlanList/servicePlanList.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    data : "",
    status : 0,
    recommmendLawyer : 0,
    demandLawyer : 0,
    lawyerList : [],
    lawyerNameList : [],
    pickerNameValue : 0,
    modalHidden : true,
    lawyerId : 0,
    number : "",
    note : "",
    changeLawyerApplyId : 0,
    creator : "",
    createTime : "",
    reason : "",
    modalHidden1 : true,
    result : 0,
    advice : "",
    projectUserChangeLawyerString : "",
    showAuditButton : true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      data : options.data,
      status : options.status,
      
    })
    if(options.projectUserChangeLawyerString != null){
      this.setData({
        projectUserChangeLawyerString : options.projectUserChangeLawyerString
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
    if(that.data.status == 6){
    that.initDataDistri();
    }else{
      if(that.data.status == 16){
        that.initDataArchive();
      }else{
        if(that.data.status == 12){
          if("" != that.data.projectUserChangeLawyerString){
            var entity = JSON.parse(that.data.projectUserChangeLawyerString)
            that.setData({
              showAuditButton : false,
              changeLawyerApplyId : entity.id,
              creator : entity.creator == 0? "企业":"律师",
              createTime : app.formatDate(entity.createTime),
              reason : entity.reason
            })
          }else{
          that.initDataChangeLawyer();
          }
        }
      }
    }
  },

  initDataDistri : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/api/project/demandLawyer/' + JSON.parse(that.data.data).id + '/closestRecord',
      method : 'GET',
      success : function(res){
        if(res.data.code ==0){
          that.setData({
            demandId : res.data.projectCompanyDemandLawyer.id,
            recommmendLawyer :res.data.projectCompanyDemandLawyer.recommendLawyer,
            demandLawyer : res.data.projectCompanyDemandLawyer.demandLawyer
          })
        }
      }
    })
  },
  distributeLawyer : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/api/user/lawyer/list',
      method : 'GET',
      success :function(res){
        if(res.data.code == 0){
          that.setData({
            lawyerList : res.data.page.list
          })
          var temp = [];
          for(var i = 0; i < res.data.page.list.length; i++){
            temp[temp.length] = res.data.page.list[i].name
          }
          that.setData({
            lawyerNameList : temp,
            modalHidden : false
          })
        }
      }
    })
  },
  bindNamePickerChange : function(e){
    this.setData({
      pickerNameValue : e.detail.value,
      lawyerId : this.data.lawyerList[e.detail.value].account
    })
  },
  modalCandel : function(){
    this.setData({
      modalHidden : true
    })
  },
  modalConfirm : function(){
    var that = this;
    if(that.data.lawyerId == 0) {
      wx.showModal({
        title : '提示',
        content : "请选择律师",
        showCancel : false
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl +'/api/project/' +  JSON.parse(that.data.data).id + '/distributeLawyer',
        method : 'POST',
        data : {
          demandLawyerRecordId : that.data.demandId,
          lawyerId : that.data.lawyerId
        },
        success : function(res){
          console.log(res.data)
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
  initDataArchive : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl +'/api/project/archive/' + JSON.parse(that.data.data).id + '/info',
      method : 'GET',
      success : function(res){
        if(res.data.code == 0){
          that.setData({
            number : res.data.projectArchive.archiveId,
            note : res.data.projectArchive.note
          })
        }else{
          wx.showToast({
            title: '获取数据失败',
            icon : 'fail',
            duration : 1500,
            complete : wx.navigateBack({
              delta: 1,
            })
          })
        }
      }
    })
  },
  initDataChangeLawyer : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/api/project/changeLawyer/' + JSON.parse(that.data.data).id + '/info',
      method : 'GET',
      success : function(res){
        if(res.data.code == 0){
          that.setData({
            changeLawyerApplyId : res.data.projectUserChangeLawyer.id,
            creator : res.data.projectUserChangeLawyer.creator == 0? "企业":"律师",
            createTime : app.formatDate(res.data.projectUserChangeLawyer.createTime),
            reason : res.data.projectUserChangeLawyer.reason
          })
        }else{
          wx.showToast({
            title: '获取数据失败',
            icon : 'fail',
            duration : 1500,
            complete : wx.navigateBack({
              delta: 1,
            })
          })
        }
      }
    })
  },
  auditChangeApplyAgree : function(){
    this.setData({
      modalHidden1 : false,
      result : 1
    })
  },
  auditChangeApplyRefuse : function(){
    this.setData({
      modalHidden1 : false,
      result : 0
    })
  },
  modalCandel1 : function(){
    this.setData({
      modalHidden1 : true
    })
  },
  inputAdvice : function(e){
    this.setData({
      advice : e.detail.value
    })
  },
  modalConfirm1 : function(){
    var that = this;
    if(that.data.advice == ""){
      wx.showModal({
        title : '提示',
        content : '审核意见不能为空',
        showCancel : false
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + '/api/project/' + JSON.parse(that.data.data).id + '/changeLawyerAudit',
        method : 'POST',
        data : {
          changeLawyer : that.data.changeLawyerApplyId,
          result : that.data.result,
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
  }
})