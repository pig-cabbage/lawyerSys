// pages/projectManagerIndex/projectManagerIndex.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    buttonText: ['待审核项目', "待分配服务项目", "待支付项目", "待分配律师项目", "待承接项目", "在办项目", "到期项目", "已归档项目", "更换律师待审核项目", "申诉待处理项目"],
    statusList : [[0], [1], [3], [6], [7], [9], [11, 15], [16], [12], [10]]
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
  changeTo : function(event){
    var index = JSON.stringify(this.data.statusList[event.currentTarget.dataset.value]);
    wx.navigateTo({
      url: '../projectListIndex/projectListIndex?index=' + index,
    })
  }
})