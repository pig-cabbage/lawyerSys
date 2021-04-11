const app = getApp();
Page({
  data: {
    bar: [{"key": "企业名称", "value": ""}, {"key": "联系方式", "value": ""}, {"key": "机构代码", "value": ""}, {"key": "企业地址", "value": ""}],
    id: 0,
    url: "",
    modalHidden: true,
    accountId : "",
    certificationTime : null,
    certificationStatus : null,
    url1 : null
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
      'bar[2].value' : item.organizationCode,
      'bar[3].value' : item.address,
      certificationTime : app.formatDate(item.certificationTime),
      url : item.businessLicense
    })
  }else{
    wx.request({
      url: app.globalData.baseUrl + '/api/user/company/info/account/' + options.id,
      method : 'GET',
      success: function(res){
        that.setData({
          accountId : options.id,
          'bar[0].value' : res.data.userCompany.name,
          'bar[1].value' : res.data.userCompany.phoneNumber,
          'bar[2].value' : res.data.userCompany.organizationCode,
          'bar[3].value' : res.data.userCompany.address,
          certificationTime : app.formatDate(res.data.userCompany.certificationTime),
          url : res.data.userCompany.businessLicense
        })
      }
    })
  }
  },
  async showPhoto(){
    var that = this;
    if(that.data.url1 == null){
      const res = await app.getFile(that.data.url);
      that.setData({
        url1 : res,
      })
    }
    
    that.setData({
      modalHidden : false,
    })
    
  },
  modalCandel : function(){
    this.setData({
      modalHidden : true
    })
  },
  modalConfirm : function(){
    this.setData({
      modalHidden : true
    })
  }
})