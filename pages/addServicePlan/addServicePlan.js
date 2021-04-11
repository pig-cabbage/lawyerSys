const app = getApp();
Page({
  data: {

    stepList: [{
      name: '基本属性'
    }, {
      name: '服务内容'
    }, {
      name: '文件模板'
    }, {
      name: '完成'
    }, ],
    stepNum: 1, //当前的步数
    level : 0,
    levelId : 0,
    chargeStandard : 0,
    planName : "",
    content : "",
    value : [],
    addPlan : "addPlan"

  },
  onLoad: function (options) {
    this.setData({
      level : options.level,
      levelId : options.levelId,
      chargeStandard : options.chargeStandard
    })
  },
  // 下一步
  numSteps : function() {
    if(this.data.stepNum == 1){
      if(this.data.planName == ""){
        wx.showModal({
          title : "提示",
          content : "服务名称不能为空",
          showModal : false
        })
      }else{
        this.setData({
          stepNum: this.data.stepNum == this.data.stepList.length ? 1 : this.data.stepNum + 1
        })
      }}else{
        if(this.data.stepNum == 2){
          if(this.data.content == ""){
            wx.showModal({
              title : "提示",
              content : "服务方案内容不能为空",
              showCancel : false
            })
          }else{
            this.setData({
              stepNum: this.data.stepNum == this.data.stepList.length ? 1 : this.data.stepNum + 1
            })
          }}else{
            this.setData({
              stepNum: this.data.stepNum == this.data.stepList.length ? 1 : this.data.stepNum + 1
            })
          }
        }
      
    
    
  },
    // 上一步
    previous : function() {
      if(this.data.stepNum == 4){
        this.setData({
          value : app.globalData.addPlanTempFile
        })
      }
      this.setData({
        stepNum: this.data.stepNum -1
      })
    },
  inputName : function(e){
    this.setData({
      planName : e.detail.value
    })
  },
  inputContent : function(e){
    this.setData({
      content : e.detail.value
    })
  },
  finish : function(e){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/api/service/plan/add',
      method : 'POST',
      data : {
        serviceLevel : that.data.levelId,
        name : that.data.planName,
        content : that.data.content,
        userInfo : app.globalData.userInfo.id
      },
      success : function(res){
        if(res.data.code == 0){
          wx.showToast({
            title: '注册成功',
            icon : 'success',
            duration : 1500,
            complete : wx.navigateBack({
              delta: 1,
            })
          })
          app.globalData.addPlanTempFile=[]
        }
      }

    })
  }
})
