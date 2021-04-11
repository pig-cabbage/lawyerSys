// app.js
App({
  globalData: {
    socketStatus: 'closed',
    userInfo: null,
    userInfoFront: null,
    baseUrl: "http://localhost:8080",
    ossUrl: "https://lawyer-sys.oss-cn-guangzhou.aliyuncs.com",
    socketUrl: "ws://localhost:8888",
    levelList: [],
    onlyLevelList: [],
    planId: 0,
    addPlanTempFile: [],
    currentFilePath: [],
    projectStatus: ["登记成功，待审核", "审核通过，待分配服务", "审核不通过", "待支付", "支付过期", "待选择律师", "待系统分配律师", "待律师承接", "待开始服务", "正在服务", "待处理申诉", "服务结束，待评价", "待审核更换律师申请", "待企业重新选择律师", "待处理更换律师申请", "服务结束，已评价", "已归档", "支付过期"]
  },
  formatDate: function (date) {
    var temp = new Date(date);
    var month = temp.getMonth() + 1;
    return temp.getFullYear() + "-" + month + "-" + temp.getDate();
  },
  formatDateAll: function (date) {
    var temp = new Date(date);
    var month = temp.getMonth() + 1;
    return temp.getFullYear() + "-" + month + "-" + temp.getDate() + " " + temp.getHours() + ":" + temp.getMinutes() + ":" + temp.getSeconds();
  },
  getLevelList: function () {
    var that = this;
    wx.request({
      url: that.globalData.baseUrl + "/api/service/level/list",
      method: "GET",
      success: function (res) {
        that.globalData.levelList = res.data.page.list
        for (var i = 0; i < that.globalData.levelList.length; i++) {
          that.globalData.onlyLevelList[that.globalData.onlyLevelList.length] = that.globalData.levelList[i].level
        }
      }
    })
    return true
  },
  formatFileName: function (name) {
    var str = name;
    var index = name.indexOf("/")
    while (index > -1) {
      str = str.substring(index + 1);
      index = str.indexOf("/")
    }
    return str
  },
  onLaunch() {
    var that = this;
    that.getLevelList();
    wx.showTabBar({
      animation: false,
    })
    // 展示本地存储能力
    // const logs = wx.getStorageSync('logs') || []
    // logs.unshift(Date.now())
    // wx.setStorageSync('logs', logs)
    // var that = this;
    // if (that.globalData.socketStatus == 'closed') {
    //   that.openSocket();
    // }
  },
  getFile: function (targetName) {
    return new Promise((resolve, reject) => {
      wx.request({
        url: this.globalData.baseUrl + "/oss/get",
        method: "GET",
        data: {
          objectName: targetName
        },
        success: function (res) {
          if (res.data.code == 0) {
            wx.downloadFile({
              url: res.data.url,
              success: function (res) {
                if (res.statusCode == 200) {
                  resolve(res.tempFilePath)
                } else {
                  wx.showModal({
                    title: "提示",
                    content: "获取数据失败,请重试",
                    showCancel: false,
                    success: function (res) {
                      if (res.confirm) {
                        wx.navigateBack({
                          delta: 1,
                        })
                      }
                    }
                  })
                }
              },
              fail(err) {
                wx.showModal({
                  title: "提示",
                  content: "获取数据失败,请重试",
                  showCancel: false,
                  success: function (res) {
                    if (res.confirm) {
                      wx.navigateBack({
                        delta: 1,
                      })
                    }
                  }
                })
              }
            })
          } else {
            wx.showModal({
              title: "提示",
              content: "获取数据失败,请重试",
              showCancel: false,
              success: function (res) {
                if (res.confirm) {
                  wx.navigateBack({
                    delta: 1,
                  })
                }
              }
            })
          }
        }
      })
    })

  },

})