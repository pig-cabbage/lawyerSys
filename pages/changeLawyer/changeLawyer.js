// pages/changeLawyer/changeLawyer.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    reasonList1 : [{name : "律师消极工作", checked: false}, {name : "律师不能满足企业需求", checked: false}, {name : "时间冲突", checked: false}, {name : "律师服务态度恶劣", checked: false}, {name:"想选择其他律师",checked: false}, {name:"其他", checked: false}],
    reasonList2 : [{name : "律师不能满足企业需求", checked: false}, {name : "企业不配合律师的工作", checked: false}, {name : "时间冲突", checked: false}, {name : "企业沟通态度恶劣", checked: false}, {name:"企业有其他的律师选择",checked: false}, {name:"其他", checked: false}],
    reason : "",
    reasonLendth: 0,
    hidden : true
  },
  formInputChange : function(e){
    this.setData({
      reason : e.detail.value,
      reasonLength : e.detail.value.length
    })
  },
  radioChange: function (e) {
    var that = this;
    var temp = that.data.reasonList;
    var newReason = "";
    if(that.data.reason == e.detail.value){
      return;
    }else{
      for (var i = 0, len = temp.length; i < len; ++i) {
        if(temp[i].name == e.detail.value){
            newReason = e.detail.value
            temp[i].checked = true
            if(i == temp.length - 1){
              that.setData({
                hidden : false
              })
            }else{
              that.setData({
                hidden : true
              })
            }
        }else{
          temp[i].checked = false;    
        }
      }
      that.setData({
        reasonList : temp,
        reason : newReason
      })
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      project : options.id,
      lawyer : options.lawyer
    })
    if(options.role == "0"){
      that.setData({
        reasonList : that.data.reasonList1,
        role : 0,
      })
    }else{
      that.setData({
        reasonList : that.data.reasonList2,
        role : 1,
      })
    }
  },

  submitForm : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/" + that.data.project + "/user/changeLawyer",
      method : "POST",
      data : {
        reason : that.data.reason,
        creator : that.data.role
      },
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success : function(res){
        if(res.data.code == 0){
          wx.showModal({
            title : "提示",
            content : "操作成功, 请等待系统处理",
            showCancel : false,
            success : function(res){
              if(res.confirm){
                var pages = getCurrentPages();
                var prevPage = pages[pages.length - 2];
                prevPage.setData({
                  message : "changeLawyer"
                })
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


})