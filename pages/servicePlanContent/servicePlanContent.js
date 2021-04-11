const app = getApp();
Page({
  data: {
    button : "编辑",
    startEditor : true
  },

  onLoad: function(options) {
    var that =  this;
    var editor = options.editor;
    
    if(editor == "false"){
      that.setData({
        height : "100%",
        editor : editor
      })
    }else{
      that.setData({
        height : "80%",
        editor : editor
      })
    }
    if(options.detail != null){
      if(options.detail == "false"){
        that.setData({
          height : "100%",
          editor : "false"
        })
      }
    }
    that.setData({
      oldValue : options.data,
      id : options.id
    }) 
  },
  startEditor : function(){
    var that = this;
    if(that.data.button == "编辑"){
    this.setData({
      startEditor : false,
      button : "保存",
      newValue : this.data.oldValue
    })
    }else{
      wx.request({
        url: app.globalData.baseUrl + "/api/service/plan/update",
        method : "POST",
        data : {
          id : that.data.id,
          content : that.data.newValue
        },
        success : function(res){
          if(res.data.code == 0){
            wx.showToast({
              title: '修改成功',
              icon : 'success',
              duration : 1500,
              complete : that.setData({
                oldValue : that.data.newValue,
                startEditor : true,
      button : "编辑",
              })
            })
          }
        }
      })
    }
  },
  inputContent : function(e){
    this.setData({
      newValue : e.detail.value
    })
  }
  
})
