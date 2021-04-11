// pages/downloadFile/downloadFile.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var targetName = options.url
    this.getFile(targetName);
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

  getFile : function(targetName){
    var that = this;
    wx.request({
      url: app.globalData.baseUrl + "/oss/get",
      method : "GET",
      data : {
        objectName : targetName
      },
      success : function(res){
        if(res.data.code == 0){
          wx.downloadFile({
            url: res.data.url,
            success : function(res){        
              if(res.statusCode == 200){
                that.setData({
                  imagePath : res.tempFilePath
                })
              }else{
                wx.showModal({
                  title : "提示",
                  content : "获取数据失败,请重试",
                  showCancel : false,
                  success : function(res){
                    if(res.confirm){
                      wx.navigateBack({
                        delta: 1,
                      })
                    }
                  }
                })
              }
            },fail(res){
              wx.showModal({
                title : "提示",
                content : "获取数据失败,请重试",
                showCancel : false,
                success : function(res){
                  if(res.confirm){
                    wx.navigateBack({
                      delta: 1,
                    })
                  }
                }
              })
            }
          })
        }else{
          wx.showModal({
            title : "提示",
            content : "获取数据失败,请重试",
            showCancel : false,
            success : function(res){
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

})