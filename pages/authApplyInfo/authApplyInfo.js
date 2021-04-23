const app = getApp();
Page({
  data: {
    bar1: [{"key": "企业名称", "value": ""}, {"key": "联系方式", "value": ""}, {"key": "机构代码", "value": ""}, {"key": "企业地址", "value": ""}],
    bar2:[{"key": "姓名", "value": ""}, {"key": "联系方式", "value": ""}, {"key": "性别", "value": ""}, {"key": "学位", "value": ""}, {"key": "从业时长", "value": ""}],
    role: 0,
    bar: [],
    id: 0,
    businessLicense: "",
    positiveIdCard: "",
    negativeIdCard: "",
    lawyerLicense: "",
    modalHidden: true,
    result: 0,
    advice: "",
    lowestLevel: 0,
    highestLevel: 0,
    array:[0,1,2,3,4,5,6,7,8,9,10],
    modalHidden1: true,
    pickerLowValue: 0,
    pickerHighValue: 0
  },
  onLoad: function(options) {
    var item = JSON.parse(options.data)
    var role = options.role
    if(role == 0){
      this.setData({
        id: item.id,
        bar: this.data.bar1,
        'bar[0].value' : item.name,
        'bar[1].value' : item.phoneNumber,
        'bar[2].value' : item.organizationCode,
        'bar[3].value' : item.address,
        businessLicense : item.businessLicense
      })
    }else{
      this.setData({
        id: item.id,
        role: 1,
        bar:this.data.bar2,
        'bar[0].value' : item.name,
        'bar[1].value' : item.phoneNumber,
        'bar[2].value' : item.sex == 0? "女":"男",
        'bar[3].value' : item.degree == 0? "学士":(item.degree == 1? "硕士":(item == 2? "博士":"其他")),
        'bar[4].value' : item.workTime,
        positiveIdCard : item.positiveIdCard,
        negativeIdCard : item.negativeIdCard,
        lawyerLicense : item.lawyerLicense,
        array : app.globalData.onlyLevelList
      })
    };
  },
  showAdvice: function(event){
    this.setData({
      result: event.currentTarget.dataset.result,
      modalHidden: false
    })
  },
  inputAdvice: function(e){
    this.setData({
      advice: e.detail.value
    })
  },
  modalCandel: function(){
    this.setData({
      advice: "",
      modalHidden: true
    })
  },
  modalConfirm: function(){
    if(this.data.role == 0){
      wx.request({
        url: app.globalData.baseUrl + "/api/process/user/company/" + this.data.id + "/auth",
        method: "POST",
        data:{
          result : this.data.result,
          advice: this.data.advice 
        },
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
      success: function(res){
        if(res.data.code == 0){
          wx.showToast({
            title: '处理成功',
            icon: 'success',
            duration: 1500,
            complete: wx.navigateBack({
              delta: 1
             })
          })
        }
      },
      })
    }else{
      if(this.data.result == 0){
        wx.request({
          url: app.globalData.baseUrl + "/api/process/user/lawyer/" + this.data.id + "/auth",
          method: "POST",
          data:{
            result : this.data.result,
            advice: this.data.advice 
          },
          header : {
            'cookie' : wx.getStorageSync("sessionid")
          },
        success: function(res){
          if(res.data.code == 0){
            wx.showToast({
              title: '处理成功',
              icon: 'success',
              duration: 1500,
              complete: wx.navigateBack({
                delta: 1
               })
            })
          }
        },
        })
      }else{
        this.setData({
          modalHidden1: false
        })
      }
    }
  },
  bindLowPickerChange: function(e){
    this.setData({
      pickerLowValue: e.detail.value,
      lowestLevel: this.data.array[e.detail.value]
    })
  },
  bindHighPickerChange: function(e){
    this.setData({
      pickerHighValue: e.detail.value,
      highestLevel: this.data.array[e.detail.value]
    })
  },
  modalCandel1: function(){
    this.setData({
      modalHidden1: true
    })
  },
  modalConfirm1: function(){
    if(this.data.lowestLevel > this.data.highestLevel){
      wx.showToast({
        title: '等级设置错误',
        icon: 'error',
        duration: 1500
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + "/api/process/user/lawyer/" + this.data.id + "/auth",
        method: "POST",
        data:{
          result : this.data.result,
          advice: this.data.advice,
          lowestLevel: this.data.lowestLevel,
          highestLevel: this.data.highestLevel
        },
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
      success: function(res){
        if(res.data.code == 0){
          wx.showToast({
            title: '处理成功',
            icon: 'success',
            duration: 1500,
            complete: wx.navigateBack({
              delta: 1
             })
          })
        }else{
          console.log(res)
        }
      },
      })
    }
  }

})