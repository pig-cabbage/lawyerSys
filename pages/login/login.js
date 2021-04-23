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
  },
  onShow(){
    if(app.globalData.userInfo != null){
      app.logout();
    }
  },
  login(event) {
    var that = this;
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
            wx.setStorageSync("sessionid", res.header["Set-Cookie"])
            app.globalData.userInfo = JSON.parse(res.data.userInfo)
            app.getLevelList();
            that.openSocket();
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
            if(event.currentTarget.dataset.value == 0){
              wx.showModal({
                title : '提示',
                content : '您不是管理员',
                showCancel : 'false'
              })
            }else{
              wx.showModal({
                title : '提示',
                content : '登录身份错误',
                showCancel : 'false'
              })
            }
          }
        }
        })
      }
    })
  },
  openSocket() {
    var that = this;
    //监听 WebSocket 连接打开事件
    wx.onSocketOpen((result) => {
      console.log('WebWocket 已连接');
      app.globalData.socketStatus = 'connected';
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
    wx.onSocketMessage(onMessage => {
      console.log('服务器返回的消息First: ', onMessage.data)
    })
    //创建一个 WebSocket 连接
    wx.connectSocket({
      url: app.globalData.socketUrl + "/api/im/" +app.globalData.userInfo.id,
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success(res){
        console.log("连接成功")
      },
      fail(res){
        console.log(res)
        console.log("连接失败")
      }
    })
  },



})