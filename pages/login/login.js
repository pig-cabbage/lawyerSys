// index.js
// 获取应用实例
const app = getApp()

Page({
  data: {
    loginRole: ['管理员登录', "企业登录", "律师登录"],
  },
  // 事件处理函数
  bindViewTap() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad() {
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse) {
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },
  login(event) {
    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        wx.request({
          url: app.globalData.baseUrl + "/login",
          method: "POST",
          data: {
            jsCode: res.code,
            role: event.currentTarget.dataset.value
          },
          success(res){
            if(res.data.code == 0){
            app.globalData.userInfo = JSON.parse(res.data.userInfo)
            switch(event.currentTarget.dataset.value){
              case(0):
                wx.navigateTo({
                  url: '../systemIndex/systemIndex',
                })
                break;
              case(1):
                wx.navigateTo({
                  url: '../companyIndex/companyIndex',
                })
                break;
              default:
                wx.navigateTo({
                  url: '../lawyerIndex/lawyerIndex',
                })
                break;
            }
          }else{
            wx.showModal({
              title : '提示',
              content : '登录身份错误',
              showCancel : 'false'
            })
          }
        }
        })
      }
    })
  },
  openSocket() {
    //监听 WebSocket 连接打开事件
    wx.onSocketOpen((result) => {
      console.log('WebWocket 已连接');
      app.globalData.socketStatus = 'connected';
      this.sendMessage();
    })
    //监听 WebSocket 连接关闭事件
    wx.onSocketClose(() => {
      console.log('WebSocket 已断开');
      app.globalData.socketStatus = 'closed';
    })
    //监听 WebSocket 错误事件
    wx.onSocketError(error => {
      console.error('socket error:', error)
    })
    //监听 WebSocket 接受到服务器的消息事件
    wx.onSocketMessage(message => {
      //把JSONStr转为JSON
      message = message.data.replace(" ", "");
      if (typeof message != 'object') {
        message = message.replace(/\ufeff/g, ""); //重点
        var jj = JSON.parse(message);
        message = jj;
      }
      console.log("【websocket监听到消息】内容如下：");
      console.log(message);
    })
    //创建一个 WebSocket 连接
    wx.connectSocket({
      url: app.globalData.socketUrl
    })
  },
  //发送消息函数
  sendMessage() {
    if (app.globalData.socketStatus === 'connected') {
      //自定义的发给后台识别的参数 ，我这里发送的是name
      wx.sendSocketMessage({
        data: "{\"name\":\"" + app.globalData.userInfo.id + "\"}"
      })
    }
  },
  //关闭信道
  closeSocket() {
    if (app.globalData.socketStatus === 'connected') {
      wx.closeSocket({
        success: () => {
          app.globalData.socketStatus = 'closed'
        }
      })
    }
  },

})