// pages/lawyerWork/lawyerWork.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentTab : 1,
    //这里只做tab名和显示图标
    items: [
     {
       "text": "消息",
       "iconPath": "/icon/message.png",
       "selectedIconPath": "/icon/message_select.png"
     },
     {
       "text": "业务",
       "iconPath": "/icon/history.png",
       "selectedIconPath": "/icon/history_select.png"
     },
     {
       "text": "我的",
       "iconPath": "/icon/info.png",
       "selectedIconPath": "/icon/info_select.png"
     }
   ],
   bar: [{"key": "新分配", "value": "新分配项目"}, {"key": "在办", "value": "在办项目"}, {"key": "已结束", "value": "已结束项目"}],
  },

  swichNav: function (e) {
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      switch(e.target.dataset.current){
        case 0:
          wx.redirectTo({
            url: '../lawyerIndex/lawyerIndex'
          });
          break;
        case 2:
          wx.redirectTo({
            url: '../userLawyerInfo/userLawyerInfo',
          })
          break;
      }
    }
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