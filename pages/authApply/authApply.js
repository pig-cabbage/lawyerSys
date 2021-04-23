// pages/authApply/authApply.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    showTopTips: false,
    region: ['广东省', '广州市', '番禺区'],
    customItem: '全部',
    formData:{businessLicense : ""},
    rules: [{
      name: 'name',
      rules: {required: true, message: '企业名称是必选项'},
  },
   {
      name: 'phoneNumber',
      rules: [{required: true, message: '联系方式必填'}, {mobile: true, message: '联系方式格式不对'}],
  }, {
      name: 'organizationCode',
      rules: {required: true, message: '机构代码必填'},
  }, {
      name: 'address',
      rules: {required: true, message: '地址必填'},
  }, {
    name: 'businessLicense',
    rules: {required: true, message: '请上传营业执照'},
}]
  },
  formInputChange(e) {
    const {field} = e.currentTarget.dataset
    this.setData({
        [`formData.${field}`]: e.detail.value
    })
    if(field == "address"){
      this.setData({
        addressLength : e.detail.value.length
      })
    }
},

  bindAddressChange: function (e) {
    this.setData({
      region: e.detail.value
    })
  },

  chooseFile : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/oss/policy",
      method : "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success : function(res){
        if(res.data.code == 0){
          var data = res.data.data
          wx.chooseImage({
            success : function(res){
              const tempFilePath = res.tempFilePaths[0];
              var key ="user/company" +  data.dir  + app.globalData.userInfo.id + "/"+  app.formatFileName(tempFilePath);
              wx.uploadFile({
                filePath: tempFilePath,
                name: "file",
                url: 'https://lawyer-sys.oss-cn-guangzhou.aliyuncs.com' ,
                header: {
                  "Content-Type": "multipart/form-data",
                },
                method : "POST",
                formData : {
                  key : key,
                  policy : data.policy,
                  OSSAccessKeyId : data.accessid,
                  signature : data.signature,
                  success_action_status:"200",
                  // callback : data.callback
                },
                success(res){
                    that.setData({
                      [`formData.businessLicense`] : key
                    })     
                },
                fail(res){
                  wx.showToast({
                    title: '上传失败',
                    icon: 'error',
                    duration : 1500
                  })
                }
              })
            }
          })
        }
      }
    })
  },
  submitForm() {
    var that = this;
    that.selectComponent('#form').validate((valid, errors) => {
        if (!valid) {
            const firstError = Object.keys(errors)
            if (firstError.length) {
                that.setData({
                    error: errors[firstError[0]].message
                })

            }
        } else {
            wx.request({
              url: app.globalData.baseUrl + "/api/user/company/auth/apply",
              method: "POST",
              data : {
                account : app.globalData.userInfo.id,
                name : that.data.formData.name,
                phoneNumber : that.data.formData.phoneNumber,
                organizationCode : that.data.formData.organizationCode,
                address : that.data.formData.address,
                businessLicense : that.data.formData.businessLicense
              },
              header : {
                'cookie' : wx.getStorageSync("sessionid")
              },
              success : function(res){
                if(res.data.code == 0){
                  wx.showToast({
                    title: '申请成功',
                    icon : 'success',
                    duration : 1500,
                    complete : wx.navigateBack({
                      delta: 1,
                    })
                  })
                }else{
                  wx.showToast({
                    title: '申请失败',
                    icon : 'error',
                    duration : 1500
                  })
                }
              }
            })
            
        }
    })
},

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    if(options.certificationStatus == "1"){
      wx.showModal({
        title : "提示",
        content : "您已提交申请，请等待系统审核。",
        showCancel : false,
        success(res){
          if(res.confirm){
            wx.navigateBack({
              delta: 1,
            })
          }
        }
      })
    }else{
      if(options.certificationStatus == "2"){
        wx.showModal({
          title : "提示",
          content : "您已认证通过，是否修改认证信息",
          success(res){
            if(res.cancel){
              wx.navigateBack({
                delta: 1,
              })
            }else{

              wx.request({
                url: app.globalData.baseUrl + "/api/user/company/auth/" + app.globalData.userInfo.id +"/latestRecord" ,
                method : "GET",
                header : {
                  'cookie' : wx.getStorageSync("sessionid")
                },
                success : function(res){
                  if(res.data.code == 0){
                    var item = res.data.entity
                    that.setData({
                      [`formData.name`] : item.name,
                      [`formData.phoneNumber`] : item.phoneNumber,
                      [`formData.organizationCode`] : item.organizationCode,
                      [`formData.address`] : item.address,
                      [`formData.businessLicense`] : item.businessLicense,
                    })
                  }
                }
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


})