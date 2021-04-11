// pages/chat/chat.js
const app = getApp();
Page({
  /**
   * 页面的初始数据
   */
  data: {
    user_input_text: '',//用户输入文字
    inputValue: '',
    returnValue: '',
    addImg: false,
    otherAccount : "",
    //格式示例数据，可为空
    allContentList: [],
    num: 0

  },
  bottom: function () {
    var that = this;
    this.setData({
      scrollTop: 1000000
    })
  },
  // 提交文字
  submitTo: function (e) {
    var that = this;
    var data = {
      sessionId : that.data.sessionId,
      sender : that.data.sender,
      type : 0,
      content: that.data.inputValue,
      man : that.data.otherAccount
    }
 
    if (app.globalData.socketStatus == 'connected') {
      //如果打开了socket就发送数据给服务器
      sendSocketMessage(data)
      this.data.allContentList.push({ is_my: { text: this.data.inputValue }});
      this.setData({
        allContentList: this.data.allContentList,
        inputValue: ''
      })
 
      that.bottom()
    }
  },
  bindKeyInput: function (e) {
    this.setData({
      inputValue: e.detail.value
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      sessionId : options.session,
      sender : options.sender,
      otherAccount : options.other
    })
    this.bottom();
  },
  onShow : function(){
  },

  getLatest : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/chat/" + that.data.sessionId + "/latest",
      method : "GET",
      success : function(res){
        if(res.data.code == 0){
            var list = res.data.list
            for(var i = 0; i < list.length; i++){
              if(that.data.sender == 0){
                if(list[i].sender == 0){
                  that.data.allContentList.push({ is_my: { text: list[i].content }});
                }else{
                  that.data.allContentList.push({ is_ai: true, text: list[i].content });
                }
              }else{
                if(list[i].sender == 0){
                  that.data.allContentList.push({ is_ai: true, text: list[i].content });
                }else{
                  that.data.allContentList.push({is_my: { text: list[i].content } });
                }
              }
              
            }
            that.setData({
              allContentList : that.data.allContentList
            })
        }else{
          wx.showModal({
            title : "提示",
            content : "获取数据失败，请重试",
            showCancel : false,
            success(res){
              if(res.confirm){
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


  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.getLatest();
    wx.onSocketMessage(onMessage => {
      console.log('监听WebSocket接受到服务器的消息事件。服务器返回的消息', JSON.parse(onMessage.data))
      var onMessage_data = JSON.parse(onMessage.data)
      if (onMessage_data.cmd == 1) {
        that.setData({
          link_list: text
        })
        console.log(text, text instanceof Array)
        // 是否为数组
        if (text instanceof Array) {
          for (var i = 0; i < text.length; i++) {
            text[i]
          }
        } else {
        }
        that.data.allContentList.push({ is_ai: true, text: onMessage_data.body });
        that.setData({
          allContentList: that.data.allContentList
        })
        that.bottom()
      }
    })
  },
})
function sendSocketMessage(msg) {
  console.log('通过 WebSocket 连接发送数据', JSON.stringify(msg))
  wx.sendSocketMessage({
    data: JSON.stringify(msg),
    function (res) {
      console.log('已发送', res)
    }
  })
} 