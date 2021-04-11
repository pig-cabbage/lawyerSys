// index.js
// 获取应用实例
const app = getApp()

Page({
  data: {
    buttonText: ['用户管理', "服务方案管理", "项目流程管理", "数据统计", "案件管理"],
  },

  onLoad : function(){
  },
  // 事件处理函数
 changeTo(event){
   let index = event.currentTarget.dataset.value;
   switch(index){
     case 0:
       wx.navigateTo({
        url: '../userManagerIndex/userManagerIndex'
       });
       break;
     case 1:
       wx.navigateTo({
        url: '../serviceManagerIndex/serviceManagerIndex'
       });
       break;
     case 2:
       wx.navigateTo({
        url: '../projectManagerIndex/projectManagerIndex'
       });
       break;  
     case 3:
       wx.navigateTo({
        url: '../dataStatisticsIndex/dataStatisticsIndex'
       });
       break;  
     default:
       wx.navigateTo({
        url: '../logs/logs'
       });
       break;   

   }
 }

  
})
