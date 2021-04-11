// pages/addProject/addProject.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentTab : 1,
    items: [
      {
        "text": "消息",
        "iconPath": "/icon/message.png",
        "selectedIconPath": "/icon/message_select.png"
      },
      {
        "text": "发起 咨询",
        "iconPath": "/icon/add.png",
        "selectedIconPath": "/icon/add_select.png"
      },
      {
        "text": "历史咨询",
        "iconPath": "/icon/history.png",
        "selectedIconPath": "/icon/history_select.png"
      },
      {
        "text": "我的",
        "iconPath": "/icon/info.png",
        "selectedIconPath": "/icon/info_select.png"
      }
    ],
    stepList: [{
      name: '咨询需求'
    }, {
      name: '服务方案'
    }],
    stepNum: 1, //当前的步数
    content : "",
    currentIndexNav : 0,
    levelList : [],
    oldIndex : -1,
    planId : -1,
    modalHidden : true
  },

  swichNav: function (e) {
    let that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      })
      switch(e.target.dataset.current){
        case 0:
          wx.redirectTo({
            url: '../companyIndex/companyIndex',
          })
          break;
        case 2:
          if(app.globalData.userInfo.certificationStatus <= 1){
            wx.showModal({
              title : "提示",
              content : "您还未认证。",
              showCancel : false,
            })
          }else{
          wx.redirectTo({
            url: '../projectHistory/projectHistory',
          })
        }
          break;
        case 3:
          wx.redirectTo({
            url: '../userCompanyInfo/userCompanyInfo',
          })
          break;
      }
    }
  },
  numSteps : function(){
    var that = this;
    if(that.data.content == ""){
      wx.showModal({
        title : "提示",
        content : "咨询需求不能为空",
        showCancel : false
      })
    }else{
      that.setData({
        stepNum: that.data.stepNum + 1
      })
    }
  },
  inputContent : function(e){
    var that = this;
    that.setData({
      content : e.detail.value
    })
  },

  activeNav: function(e){
    var that = this;
    if(that.data.currentIndexNav != e.currentTarget.dataset.index){
    that.setData({
      currentIndexNav: e.currentTarget.dataset.index
    })
    if(that.data.currentIndexNav == 1){
      if(that.data.levelList.length == 0){
        var temp = app.globalData.levelList;
        for(var i = 0; i < temp.length; i++){
          temp[i].hideHidden = true
          temp[i].children = []
        }
        that.setData({
          levelList : temp
        })
    }
    }
  }},
  clickMenu : function(event){
    var that = this;
    var index = event.currentTarget.dataset.index;
    var oldItem = 'levelList[' + that.data.oldIndex + '].hideHidden';
    if(that.data.oldIndex == index){
      that.setData({
        [oldItem] : !that.data.levelList[index].hideHidden
      })
    }else{
      var newItem = 'levelList[' + index + '].hideHidden';
      if(that.data.levelList[index].children.length == 0){
        wx.request({
          url: app.globalData.baseUrl + "/api/service/plan/" + that.data.levelList[index].id + "/search",
          method : 'GET',
          success :function(res){
            if(res.data.code == 0){
              var temp = 'levelList[' + index + '].children';
              that.setData({
                [temp] : res.data.list,
              })
            }else{
              wx.showToast({
                title: '获取数据失败',
                icon : 'error',
                duration : 1500
              })
            }
          }
        })
      }if(that.data.oldIndex != -1){
        that.setData({
          [oldItem] : true
        })
      }
      that.setData({
        [newItem] : false,
        oldIndex : index
      })
    }
   
  },
  radioChange: function (e) {
    var that = this;
    var radioItems = that.data.levelList[that.data.oldIndex].children;
    var str = "that.data.levelList[" + that.data.oldIndex + "].children";
    for (var i = 0, len = radioItems.length; i < len; ++i) {
        radioItems[i].checked = radioItems[i].id == e.detail.value;    
    }
    that.setData({
      [str] : radioItems,
      planId : e.detail.value
    })
  },
  finish : function(){
    this.setData({
      modalHidden :false,
    })
  },
  modalCandel : function(){
    this.setData({
      modalHidden : true
    })
  },
  inputName : function(e){
    this.setData({
      projectName : e.detail.value,
      nameLength : e.detail.value.length
    })
  },
  modalConfirm : function(){
    var that = this;
    if(that.data.currentIndexNav == 0){
      wx.request({
        url: app.globalData.baseUrl + "/api/project/company/demand/add",
        method : "POST",
        data : {
          recommendPlan : 1,
          content : that.data.content,
          projectName : that.data.projectName,
          companyAccount : app.globalData.userInfo.id
        },
        success : function(res){
          if(res.data.code == 0){
            wx.navigateTo({
              url: '../projectHistory/projectHistory?currentTab=2',
            })
          }else{
            wx.showToast({
              title: '操作失败',
              icon : 'error',
              duration : 1500
            })
          }
        }
      })
    }else{
      if(that.data.planId == -1){
        wx.showModal({
          title : "提示",
          content : "请选择服务方案",
          showModal : false
        })
      }else{
        wx.request({
          url: app.globalData.baseUrl + "/api/project/company/demand/add",
          method : "POST",
          data : {
            recommendPlan : 0,
            demandPlan : that.data.planId,
            content : that.data.content,
            companyAccount : app.globalData.userInfo.id,
            projectName : that.data.projectName,
          },
          success : function(res){
            if(res.data.code == 0){
              wx.navigateTo({
                url: '../projectHistory/projectHistory?currentTab=2',
              })
            }else{
              wx.showToast({
                title: '操作失败',
                icon : 'error',
                duration : 1500
              })
            }
          }
        })
      }
    }
    
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
 const tabs = [
      {
        title: '系统推荐'
      },
      {
        title: '自主选择'
      }
    ]
    
    this.setData({ tabs })
  },
})