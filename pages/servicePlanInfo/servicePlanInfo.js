// pages/servicePlanInfo/servicePlanInfo.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    bar: [{"key": "服务名称", "value": ""}, {"key": "服务等级", "value": ""}],
    content:"",
    ids: [],
    modalHidden:true,
    modalHidden1: true,
    modifyKey:"",
    oldValue:"",
    newValue:"",
    array:[],
    array1:[],
    index:0,
    newLevelId : 0,
    status:0,
    detail:true,
    chargeStandard: 0,
    createTime : null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    if(options.data != null){
    var item = JSON.parse(options.data)
    that.setData({
      'ids[0]' : item.id,
      'bar[0].value' : item.name,
      'bar[1].value' : options.level,
      createTime : app.formatDate(item.createTime),
      chargeStandard : options.chargeStandard,
      status : item.status == 0? false : true,
      content : item.content,
    })
  }else{
    wx.request({
      url: app.globalData.baseUrl + '/api/service/plan/detail/' + options.id,
      method : 'GET',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      success: function(res){
        that.setData({
          detail : false,
          'ids[0]' : res.data.servicePlanDetail.id,
          'bar[0].value' : res.data.servicePlanDetail.name,
          'bar[1].value' : res.data.servicePlanDetail.level,
          chargeStandard : res.data.servicePlanDetail.chargeStandard,
          content : res.data.servicePlanDetail.content,
        })
      }
    })
  }
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
  deleteService: function(){
    var that = this;
    wx.showModal({
      title: '提示',
      content: '删除服务方案对应的文件模板也会被删除，确定删除吗',
      success (res) {
        if (res.confirm) {
          wx.request({
            url: app.globalData.baseUrl + '/api/service/plan/delete?id=' + that.data.ids[0],
            method: 'DELETE',
            header : {
              'cookie' : wx.getStorageSync("sessionid")
            },
            success: function(res){
              if(res.data.code == 0){
                wx.showToast({
                  title: '删除成功',
                  icon: 'cuccess',
                  complete: wx.navigateBack({
                    delta: 1,
                  })
                })
              }else{
                console.log(res)
              }
            }
          })
        } else if (res.cancel) {
        }
      }
    })
   
  },
  modifyValue: function(event){
    if(this.data.detail == false){
      return;
    }
    if(event.currentTarget.dataset.key == '服务名称'){
    this.setData({
      modifyKey: event.currentTarget.dataset.key,
      oldValue: event.currentTarget.dataset.value,
      modalHidden: false,
    })
  }else{
    if(event.currentTarget.dataset.key == '服务等级'){
      var temp = [];
      var levelList = app.globalData.levelList;
      for(var i = 0; i< levelList.length; i ++){
        temp[temp.length] = levelList[i].level;
      }
      this.setData({
        oldValue: event.currentTarget.dataset.value,
        array:levelList,
        array1 : temp,
        modalHidden1: false,
      })
    }
  }
  },
  modalCandel: function(){
    this.setData({
      modalHidden: true
    })
  },
  inputNewValue: function(e){
    this.setData({
      newValue: e.detail.value
    })
  },
  bindPickerChange:function(e){
    this.setData({
      index : e.detail.value,
      newValue : this.data.array[e.detail.value].level,
      newLevelId : this.data.array[e.detail.value].id
    })
  },
  modalConfirm: function(){
    var that = this;
    if(that.data.newValue == ""){
      wx.showToast({
        title: '新值不能为空',
        icon: 'fail',
        duration: 1500
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + '/api/service/plan/update',
        method: 'POST',
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
        data:{
          id : that.data.ids[0],
          name : that.data.newValue 
        },
        success:function(res){
          if(res.data.code == 0){
            wx.showToast({
              title: '修改成功',
              icon: 'success',
              duration: 1500
            })
            that.setData({
              modalHidden:true,
              'bar[0].value' : that.data.newValue
            })
          }else{
            wx.showToast({
              title: '修改失败',
              icon: 'fail',
              duration: 1500
            })
          }
        }
      })     
    }
  },
  modalCandel : function(){
    this.setData({
      modalHidden : true,
    })
  },
  modalConfirm1 : function(){
    var that = this;
    if(that.data.newValue == ""){
      wx.showToast({
        title: '新值不能为空',
        icon: 'fail',
        duration: 1500
      })
    }else{
      wx.request({
        url: app.globalData.baseUrl + '/api/service/plan/update',
        method: 'POST',
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
        data:{
          id : that.data.ids[0],
          serviceLevel : that.data.newLevelId 
        },
        success:function(res){
          if(res.data.code == 0){
            wx.showToast({
              title: '修改成功',
              icon: 'success',
              duration: 1500
            })
            that.setData({
              modalHidden1:true,
              'bar[1].value' : that.data.newValue,
              'bar[3].value' : that.data.array[that.data.index].chargeStandard
            })
          }else{
            wx.showToast({
              title: '修改失败',
              icon: 'fail',
              duration: 1500
            })
          }
        }
      })     
    }
  },
  modalCandel1 : function(){
    this.setData({
      modalHidden1:true,
    })
  },
  switch1Change : function(e){
    var that = this;
    that.setData({
      status : e.detail.value,
    })
    wx.request({
      url: app.globalData.baseUrl + '/api/service/plan/update',
      method: 'POST',
      header : {
        'cookie' : wx.getStorageSync("sessionid")
      },
      data:{
        id : that.data.ids[0],
        status : that.data.status == false? 0 : 1 
      },
      success:function(res){
        if(res.data.code == 0){
          wx.showToast({
            title: '修改成功',
            icon: 'success',
            duration: 1500
          })
        }else{
          wx.showToast({
            title: '修改失败',
            icon: 'fail',
            duration: 1500
          })
        }
      }
    })     
  }
})