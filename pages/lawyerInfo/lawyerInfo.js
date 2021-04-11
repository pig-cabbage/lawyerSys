const app = getApp();
Page({
  data: {
    bar: [{"key": "姓名", "value": ""}, {"key": "联系方式", "value": ""}, {"key": "性别", "value": ""}, {"key": "学位", "value": ""}, {"key": "从业时长", "value": ""}],
    id: 0,
    url: "",
    modalHidden: true,
    detail :true
  },

  onLoad: function(options) {
    var that = this;
    if(options.certificationStatus != null){
      var status = "";
      switch(options.certificationStatus){
        case "0" : 
          status = "未认证";
          wx.showModal({
            title : "提示",
            content : "您还未认证。",
            showCancel : false,
            success : function(res){
              if(res.confirm){
                wx.navigateBack({
                  delta: 1,
                })
              }
            }
          })
          break;
        case "1" :
          status = "审核中";
          break;
        case "2" : 
          status = "认证通过";
          break;
        case "3":
          status = "认证失败";
          break;
      }
      that.setData({
        certificationStatus : status
      })
    }
    if(options.data != null){
      var item = JSON.parse(options.data)
    that.setData({
      'bar[0].value' : item.name,
      'bar[1].value' : item.phoneNumber,
      'bar[2].value' : item.sex == 0? "女":"男",
      'bar[3].value' : item.degree == 0? "学士":(item.degree == 1? "硕士":(item == 2? "博士":"其他")),
      'bar[4].value' : item.workTime,
      certificationTime : app.formatDate(item.certificationTime)
    })
  }else{
    wx.request({
      url: app.globalData.baseUrl + '/api/user/lawyer/info/account/' + options.id,
      method : 'GET',
      success: function(res){
        that.setData({
          detail : false,
          'bar[0].value' : res.data.userLawyer.name,
          'bar[1].value' : res.data.userLawyer.phoneNumber,
          'bar[2].value' : res.data.userLawyer.sex == 0? "女":"男",
          'bar[3].value' : res.data.userLawyer.degree == 0? "学士":(res.data.userLawyer.degree == 1? "硕士":(res.data.userLawyer == 2? "博士":"其他")),
          'bar[4].value' : res.data.userLawyer.workTime,
          certificationTime : app.formatDate(res.data.userLawyer.certificationTime)
        })
      }
    })
  }
  },

  showPhoto: function(){
    this.setData({
      modalHidden : false
    })
  }
})