// pages/lawyerAuthApply/lawyerAuthApply.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    showTopTips: false,
    region: ['广东省', '广州市', '番禺区'],
    sexList: ["女", "男"],
    pickerSexValue: 0,
    pickerDegreeValue: 0,
    degreeList: ["学士", "硕士", "博士", "其他"],
    customItem: '全部',
    formData:{workTime : 0},
    rules: [{
      name: 'name',
      rules: {required: true, message: '姓名必填'},
  },
   {
      name: 'phoneNumber',
      rules: [{required: true, message: '联系方式必填'}, {mobile: true, message: '联系方式格式不对'}],
  },
  {
    name: 'workTime',
    rules: [{required: true, message: '从业时长必填'}, {range: [0, 99], message: '超出从业时长范围'}],
},  {
      name: 'positiveIdCard',
      rules: {required: true, message: '请上传身份证正面'},
  },{
    name: 'negativeIdCard',
    rules: {required: true, message: '请上传身份证反面'},
}, {
    name: 'lawyerLicense',
    rules: {required: true, message: '请上传律师执照'},
}]
  },
  bindSexPickerChange : function(e){
    this.setData({
      pickerSexValue : e.detail.value
    })
  },
  bindDegreePickerChange: function(e){
    this.setData({
      pickerDegreeValue : e.detail.value
    })
  },
  chooseFile : function(event){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/oss/policy",
      method : "GET",
      success : function(res){
        console.log(res.data.data)
        if(res.data.code == 0){
          var data = res.data.data
          wx.chooseImage({
            success : function(res){
              const tempFilePath = res.tempFilePaths[0];
              var key =  "user/lawyer" + data.dir  + app.globalData.userInfo.id + "/"+  app.formatFileName(tempFilePath);
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
                  var str = 'formData.' + event.currentTarget.dataset.field;
                    that.setData({
                      [str] : key
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
              url: app.globalData.baseUrl + "/api/user/lawyer/auth/apply",
              method: "POST",
              data : {
                account : app.globalData.userInfo.id,
                name : that.data.formData.name,
                phoneNumber : that.data.formData.phoneNumber,
                sex : that.data.pickerSexValue,
                degree : that.data.pickerDegreeValue,
                workTime : that.data.formData.workTime,
                positiveIdCard : that.data.formData.positiveIdCard,
                negativeIdCard : that.data.formData.negativeIdCard,
                lawyerLicense : that.data.formData.lawyerLicense,
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
                  console.log(res)
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
  formInputChange(e) {
    const {field} = e.currentTarget.dataset
      this.setData({
        [`formData.${field}`]: e.detail.value
    })
    

},
bindAddressChange: function (e) {
  this.setData({
    region: e.detail.value
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
                url: app.globalData.baseUrl + "/api/user/lawyer/auth/" + app.globalData.userInfo.id +"/latestRecord" ,
                method : "GET",
                success : function(res){
                  if(res.data.code == 0){
                    var item = res.data.entity
                    that.setData({
                      [`formData.name`] : item.name,
                      [`formData.phoneNumber`] : item.phoneNumber,
                      [`formData.workTime`] : item.workTime,
                      pickerSexValue : item.sex,
                      pickerDegreeValue : item.degree,
                      [`formData.lawyerLicense`] : item.lawyerLicense,
                      [`formData.positiveIdCard`] : item.positiveIdCard,
                      [`formData.negativeIdCard`] : item.negativeIdCard,
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

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})