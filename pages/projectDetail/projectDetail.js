// pages/projectDetail/projectDetail.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentIndexNav: 0,
    value: [],
    fileTemplate:[],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    if(options.data != null){
    var item = JSON.parse(options.data);
    item.createTimeDetail = app.formatDateAll(item.createTime)
    item.serviceTime = app.formatDate(item.startTime) + "-" + app.formatDate(item.endTime)
    this.setData({
      item : item
    })
  }
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    const tabs = [
      {
        title: '基本信息'
      },
      {
        title: '相关文档'
      },
      {
        title: '文件模板'
      }
    ] 
    this.setData({ tabs })
    let pages = getCurrentPages();
    let currPage = pages[pages.length-1];
    if(currPage.data.message != null){
      if(currPage.data.message == "changeLawyer"){
        var str = 'item.status';
        this.setData({
          [str] : 12
        })
      }
    }
  },
  activeNav: function(e){
    var that = this;
    if(e.currentTarget.dataset.index == 1 || e.currentTarget.dataset.index == 2){
      if(that.data.item.status == 17 || that.data.item.status <= 4){
        wx.showModal({
          title: '提示',
          content : "没有权限获取",
          showCancel : false,
        })
        return;
      }
    }
    if(e.currentTarget.dataset.index == 1 && that.data.value.length == 0){
      wx.request({
        url: app.globalData.baseUrl + "/api/project/file/list",
        method : "GET",
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
        data : {
          projectId : that.data.item.id
        },
        success : function(res){
          if(res.data.code == 0){
            that.setData({
              value : res.data.list
            })
          }else{
            wx.showModal({
              title : "提示",
              content : "获取数据失败",
              showCancel : false
            })
          }
        }
      })
    }
    if(e.currentTarget.dataset.index == 2 && that.data.fileTemplate.length == 0){
      wx.request({
        url: app.globalData.baseUrl + "/api/service/file/template/" + that.data.item.plan + "/list",
        method : "GET",
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
        success : function(res){
          if(res.data.code == 0){
            that.setData({
              fileTemplate : res.data.list
            })
          }else{
            wx.showModal({
              title : "提示",
              content : "获取数据失败",
              showCancel : false
            })
          }
        }
      })
    }
    if(that.data.currentIndexNav != e.currentTarget.dataset.index){
    that.setData({
      currentIndexNav: e.currentTarget.dataset.index
    })
  }
},
changeLawyer : function(){
    var that = this;
    wx.navigateTo({
      url: '../changeLawyer/changeLawyer?id=' + that.data.item.id + "&role=0"
    })
},
pay : function(){
  var that = this;
  wx.request({
    url: app.globalData.baseUrl + "/api/todoItem/list",
    method : "GET",
    header : {
      'cookie' : wx.getStorageSync("sessionid")
    },
    data : {
      project : that.data.item.id
    },
    success : function(res){
      if(res.data.code == 0){
        var item = JSON.stringify(res.data.list[0])
        wx.navigateTo({
          url: '../todoItemDetail/todoItemDetail?data=' + item,
        })
      }
    }
  })
}
})