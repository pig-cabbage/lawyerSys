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
    num: 0,
    pageSize : 30,
    cueerntPage : 0,
    first : -1,

  },
  bottom: function () {
    var that = this;
    this.setData({
      scrollTop: 1000000,
      // chatHeight : 80
    })
  },
  focus : function(){
    var that = this;
    this.setData({
      chatHeight : 100
    })
  },
  submitTo: function (e) {
    var that = this;
    var data = {
      sessionId : that.data.sessionId,
      sender : that.data.sender,
      type : 0,
      content: that.data.inputValue,
      man : that.data.otherAccount,
      createTime : new Date().getTime()
    }
    if (app.globalData.socketStatus == 'connected') {
      //如果打开了socket就发送数据给服务器
      sendSocketMessage(data)
      this.data.allContentList.push({ is_my: { text: this.data.inputValue}, createTime : -1 });
      this.setData({
        allContentList: this.data.allContentList,
        inputValue: ''
      })
      that.bottom()
    }else{
      wx.showModal({
        title: "提示",
        content : "服务器连接已断开，请重试",
        showCancel : false,
        success(res){
          wx.navigateBack({
            delta: 1,
          })
        }
      })
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
    this.getLatest();
  },
    /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    var that = this;
    wx.onSocketMessage(onMessage => {
      console.log('服务器返回的消息: ', onMessage.data)
      var item = JSON.parse(onMessage.data);
        that.data.allContentList.push({ is_ai: true, text: item.content, createTime : -1});
        that.setData({
          allContentList: that.data.allContentList
        })
        that.bottom();
    })
    
  },
  onShow : function(){
    this.bottom();
  },
  getLatest : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/api/project/chat/" + that.data.sessionId + "/latest",
      method : "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success : function(res){
        if(res.data.code == 0){
            var list = res.data.list;
            if(list.length < 1){
              return;
            }
              var first = new Date(list[0].createTime).getTime();
              var temp = new Date(list[0].createTime).getTime();
              if(new Date().getTime() - first  < 5 * 60 * 1000){
                temp = -1;
              }
              if(that.data.sender == 0){
                if(list[0].sender == 0){
                  that.data.allContentList.push({ is_my: { text: list[0].content }, createTime : temp});
                }else{
                  that.data.allContentList.push({ is_ai: true, text: list[0].content, createTime : temp });
                }
              }else{
                if(list[0].sender == 0){
                  that.data.allContentList.push({ is_ai: true, text: list[0].content, createTime : temp });
                }else{
                  that.data.allContentList.push({is_my: { text: list[0].content }, createTime : temp});
                }
              }
            for(var i = 1; i < list.length; i++){
              var temp = new Date(list[i].createTime).getTime();
              if(temp - first  < 2 * 60 * 1000){
                temp = -1;
              }
              first = temp;
              if(that.data.sender == 0){
                if(list[i].sender == 0){
                  that.data.allContentList.push({ is_my: { text: list[i].content }, createTime : temp});
                }else{
                  that.data.allContentList.push({ is_ai: true, text: list[i].content, createTime : temp });
                }
              }else{
                if(list[i].sender == 0){
                  that.data.allContentList.push({ is_ai: true, text: list[i].content, createTime : temp });
                }else{
                  that.data.allContentList.push({is_my: { text: list[i].content }, createTime : temp});
                }
              }
              
            }
            that.setData({
              allContentList : that.data.allContentList,
              first : first
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
  getMoreMessage : function(){
    var that = this;
    console.log("dsad")
    wx.request({
      url: app.globalData.baseUrl + "/api/project/chat/" + that.data.sessionId + "/more",
      method : "GET",
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data : {
        pageSize : that.data.pageSize,
        currPage : that.data.currPage
      },
      success(res){
        if(res.data.code == 0){
          var list = res.data.page.list;
          var first = that.data.first;
          if(list.length == 0){
            return;
          }
          for(var i = 0; i < list.length; i++){
            var temp = new Date(list[i].createTime).getTime();
            if(temp - first  < 2 * 60 * 1000){
              temp = -1;
            }
            first = temp;
            if(that.data.sender == 0){
              if(list[i].sender == 0){
                that.data.allContentList.unshift({ is_my: { text: list[i].content }, createTime : temp});
              }else{
                that.data.allContentList.unshift({ is_ai: true, text: list[i].content, createTime : temp });
              }
            }else{
              if(list[i].sender == 0){
                that.data.allContentList.unshift({ is_ai: true, text: list[i].content, createTime : temp });
              }else{
                that.data.allContentList.unshift({is_my: { text: list[i].content }, createTime : temp});
              }
            }
            
          }
          that.setData({
            allContentList : that.data.allContentList,
            first : first
          })
        }else{
          wx.showModal({
            title : "提示",
            content : "获取数据失败，请重试",
            showCancel : false,
          })
        }
      }
    })
  },
  gotoMyInfo(){
    var that = this;
    if(that.data.sender == 0){
      wx.navigateTo({
        url: '../companyInfo/companyInfo?id=' + app.globalData.userInfo.id + '&certificationStatus=' + app.globalData.userInfo.certificationStatus,
      })
    }else{
      wx.navigateTo({
        url: '../lawyerInfo/lawyerInfo?id=' + app.globalData.userInfo.id + '&certificationStatus=' + app.globalData.userInfo.certificationStatus,
      })
    }
  },
  gotoHeInfo(){
    var that = this;
    if(that.data.sender == 0){
      wx.navigateTo({
        url: '../lawyerInfo/lawyerInfo?id=' + that.data.otherAccount,
      })
    }else{
      wx.navigateTo({
        url: '../companyInfo/companyInfo?id=' + that.data.otherAccount,
      })
    }
  }
})
function sendSocketMessage(msg) {
  console.log('通过 WebSocket 连接发送数据', JSON.stringify(msg))
  wx.sendSocketMessage({
    data: JSON.stringify(msg),
    success(res) {
      console.log('已发送', res)
    }
  })
  
} 