// pages/companyTodoItem/companyTodoItem.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
      resultList : [],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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
    this.initData();
  },
  initData : function(){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + '/api/todoItem/list',
      method : 'GET',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data : {
        user : app.globalData.userInfo.id
      },
      success : function(res){
        if(res.data.code == 0){
          that.setData({
            resultList : res.data.list
          })
          var temp = that.data.resultList;
          for(var i = 0; i < temp.length; i++){
            temp[i].createTime = app.formatDateAll(temp[i].createTime);
            temp[i].latestTime = app.formatDateAll(temp[i].latestTime);
          }
          that.setData({
            resultList : temp
          })
        }
      }
    })
  },
  gotoDetail : function(event){
    var data = JSON.stringify(this.data.resultList[event.currentTarget.dataset.index]);
    wx.navigateTo({
      url: "../todoItemDetail/todoItemDetail?data=" + data,
    })
  }

})