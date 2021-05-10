// components/path-view/path-view.js
const app = getApp();
import toTree from './toTree';
Component({
  properties: {
    mode: {
      type: String,
      value: 'show',
      observer: function (val) {
        this.setData({
          mode: val
        })
      }
    },
    user: {
      type: String,
      value: '',
      observer: function (val) {
        this.setData({
          user: val
        })
      }
    },
    permit: {
      type: String,
      value: '',
      observer: function (val) {
        this.setData({
          permit: val
        })
      }
    },
    itemId: {
      type: String,
      value: '',
      observer: function (val) {
        this.setData({
          id: val
        })
      }
    },
    download: {
      type: Boolean,
      value: true,
      observer: function (val) {
        this.setData({
          download: val
        })
      }
    },
    value: {
      type: Array,
      value: [],
      observer() {
        this.buildData();
        this.initView();
      }
    },

    // 非树形数据，仅在value无传参时生效
    unnormalizedValue: {
      type: Array,
      value: [],
      observer() {
        this.initView();
      }
    },
    fatherKey: {
      type: String,
      value: 'pid'
    },
    selfKey: {
      type: String,
      value: 'id'
    },
    rootValue: {
      type: null,
      value: null
    },
    pathMode: {
      type: String,
      value: 'mode1'
    },
    firstFloorTxt: {
      type: String,
      value: '根目录'
    },
    btnTxt: {
      type: String,
      value: '选择'
    },
    contentKey: {
      type: String,
      value: 'fileName'
    },

    contentId: {
      type: String,
      value: 'id'
    },
    // 保持位置
    // 如果开启，数据动态更新后，会保持和更新前父元素对应的界面一致
    // 这里是通过记录下更新前的父元素的唯一标识符：id（或是selfKey中设定的键对应的值），在更新后找回显示出来
    // 所以就算更新后路径变了（父元素本来在第四层，变成了第二层之类），仍然能显示父元素的相应界面
    keepLoc: {
      type: Boolean,
      value: false
    }
  },
  data: {
    structuredData: [],
    outValue: [],
    currentPath: [],
    // 判断当前是否已正在执行修改路径的方法
    isChange: false,
    // 映射value值，存放树形数据
    normalValue: [],
    currentFatherId: null,
    modalHidden: true,
    modalHidden1: true,
    modalHidden2: false,
    array: ["文件夹", "文件"],
    type: 0,
    newUrl: "",
    name: "",
    suffix: "",
    newList: [],
    mode: "show",
    id: -1,
    download : true,
  },
  methods: {
    newValue() {
      this.setData({
        modalHidden: false
      })
    },
    bindPickerChange(e) {
      this.setData({
        type: e.detail.value
      })
      if (1 == e.detail.value) {
        this.setData({
          modalHidden1: false,
          modalHidden2: true
        })
      } else {
        this.setData({
          modalHidden1: true,
          modalHidden2: false
        })
      }
    },
    modalCandel() {
      this.setData({
        modalHidden: true
      })
    },
    inputName(e) {
      this.setData({
        name: e.detail.value
      })
    },
    upload: function () {
      var that = this;
      var parent = that.data.currentPath.length == 0 ? -1 : this.data.currentPath[this.data.currentPath.length - 1].id;
      var outValue = that.data.outValue;
      var currentPath = that.data.currentPath;
      if (that.properties.permit == "public") {
        wx.request({
        url: app.globalData.baseUrl + "/oss/policy",
        method: "GET",
        header : {
          'cookie' : wx.getStorageSync("sessionid")
        },
        success: function (res) {
          if (res.data.code == 0) {
            var data = res.data.data
            wx.chooseMessageFile({
              success(res) {
                var item = res.tempFiles[0];     
                var index = item.name.indexOf('.');
                var name = item.name.slice(0, index);
                var type = item.name.slice(index + 1);
                var key = "service/plan/" + that.data.id + "/" + data.dir+  Math.random().toString(36).slice(-8)+"_" + item.name;
                wx.uploadFile({
                  filePath: item.path,
                  name: 'file',
                  url: app.globalData.ossUrl,
                  header: {
                    "Content-Type": "multipart/form-data",
                  },
                  method: "POST",
                  formData: {
                    key: key,
                    policy: data.policy,
                    OSSAccessKeyId: data.accessid,
                    signature: data.signature,
                    success_action_status: "200",
                  },
                  success: function (res) {
                    if (res.statusCode == 200) {
                      wx.request({
                        url: app.globalData.baseUrl + "/api/service/file/template/save",
                        method: "POST",
                        header : {
                          'cookie' : wx.getStorageSync("sessionid")
                        },
                        data: {
                          fileName: name,
                          url: key,
                          type: 1,
                          suffix: type,
                          parent: parent,
                          plan: that.data.id
                        },
                        success(res) {
                          if (res.data.code == 0) {
                            wx.showToast({
                              title: '新建成功',
                              success: "success",
                              duration: 1500,
                              complete: that.modifyValue(res.data.entity, outValue, currentPath)
                            })
                            if (that.data.mode == "addPlan") {
                              app.globalData.addPlanTempFile[app.globalData.addPlanTempFile.length] = res.data.entity
                            }
                          }
                        }
                      })
                    } else {
                      wx.showToast({
                        title: '上传失败',
                        icon: "error"
                      })
                    }
                  },
                  fail(res) {
                    wx.showToast({
                      title: '上传失败',
                      icon: "error",
                      duration: 1500
                    })
                  }
                })
              }
            })

          }
        }
      })}else{
        wx.request({
          url: app.globalData.baseUrl + "/oss/policy",
          method: "GET",
          header : {
            'cookie' : wx.getStorageSync("sessionid")
          },
          success: function (res) {
            if (res.data.code == 0) {
              var data = res.data.data
              wx.chooseMessageFile({
                success(res) {
                  var item = res.tempFiles[0];     
                  var index = item.name.indexOf('.');
                  var name = item.name.slice(0, index);
                  var type = item.name.slice(index + 1);
                  var key = "project/" + that.data.id + "/" + data.dir +  Math.random().toString(36).slice(-8)+"_" +item.name;
                  wx.uploadFile({
                    filePath: item.path,
                    name: 'file',
                    url: app.globalData.ossUrl,
                    header: {
                      "Content-Type": "multipart/form-data",
                    },
                    method: "POST",
                    formData: {
                      key: key,
                      policy: data.policy,
                      OSSAccessKeyId: data.accessid,
                      signature: data.signature,
                      success_action_status: "200",
                    },
                    success: function (res) {
                      if (res.statusCode == 200) {
                        wx.request({
                          url: app.globalData.baseUrl + "/api/project/file/save",
                          method: "POST",
                          header : {
                            'cookie' : wx.getStorageSync("sessionid")
                          },
                          data: {
                            fileName: name,
                            url: key,
                            type: 1,
                            suffix: type,
                            parent: parent,
                            project: that.data.id
                          },
                          success(res) {
                            if (res.data.code == 0) {
                              wx.showToast({
                                title: '新建成功',
                                success: "success",
                                duration: 1500,
                                complete: that.modifyValue(res.data.entity, outValue, currentPath)
                              })
                              if (that.data.mode == "addPlan") {
                                app.globalData.addPlanTempFile[app.globalData.addPlanTempFile.length] = res.data.entity
                              }
                            }
                          }
                        })
                      } else {
                        wx.showToast({
                          title: '上传失败',
                          icon: "error"
                        })
                      }
                    },
                    fail(res) {
                      wx.showToast({
                        title: '上传失败',
                        icon: "error",
                        duration: 1500
                      })
                    }
                  })
                }
              })
  
            }
          }
        })
      }
    },
    download : function(e){
      var that = this;
      if(that.data.download == false){
        wx.showModal({
          title : "提示",
          content : "你还未订购服务，无法下载。",
          showCancel : false,
        })
      }else{
        wx.request({
          url: app.globalData.baseUrl + "/oss/get",
          method : "GET",
          header : {
            'cookie' : wx.getStorageSync("sessionid")
          },
          data : {
            objectName : e.currentTarget.dataset.value
          },
          success : function(res){
            if(res.data.code == 0){
              wx.downloadFile({
                url: res.data.url,
                success : function(res){     
                  if(res.statusCode == 200){
                    wx.openDocument({
                      filePath: res.tempFilePath,
                      success(){
                            wx.showToast({
                          title: '下载成功',
                          icon : "success"
                        })
                      }
                    })
                    // wx.saveFile({
                    //   tempFilePath: res.tempFilePath,
                    //   success(res){
                    //     that.setData({
                    //       downLoadFile : res.savedFilePath
                    //     })
                    //     wx.showToast({
                    //       title: '下载成功',
                    //       icon : "success"
                    //     })
                    //   }
                    // })
                  }else{
                    wx.showModal({
                      title : "提示",
                      content : "获取数据失败,请重试",
                      showCancel : false,
                    })
                  }
                },fail(res){
                  wx.showModal({
                    title : "提示",
                    content : "获取数据失败,请重试",
                    showCancel : false,
  
                  })
                }
              })
            }else{
              wx.showModal({
                title : "提示",
                content : "获取数据失败,请重试",
                showCancel : false,
  
              })
            }
          }
  
        })
      }
      
    },
    modalConfirm() {
      var that = this;
      if (that.properties.permit == "public") {
          wx.request({
            url: app.globalData.baseUrl + "/api/service/file/template/save",
            method: 'POST',
            header : {
              'cookie' : wx.getStorageSync("sessionid")
            },
            data: {
              fileName: that.data.name,
              type: 0,
              parent: that.data.currentPath.length == 0 ? -1 : this.data.currentPath[this.data.currentPath.length - 1].id,
              plan: that.data.id
            },
            success: function (res) {
              if (res.data.code == 0) {
                wx.showToast({
                  title: '新增成功',
                  icon: 'success',
                  duration: 1500,
                  complete: that.modifyValue(res.data.entity, that.data.outValue, that.data.currentPath)
                })
                if (that.data.mode == "addPlan") {
                  app.globalData.addPlanTempFile[app.globalData.addPlanTempFile.length] = res.data.entity
                }
              }
            }
          })
      } else {
          wx.request({
            url: app.globalData.baseUrl + "/api/project/file/save",
            method: 'POST',
            header : {
              'cookie' : wx.getStorageSync("sessionid")
            },
            data: {
              project: that.data.id,
              fileName: that.data.name,
              type: 0,
              parent: that.data.currentPath.length == 0 ? -1 : this.data.currentPath[this.data.currentPath.length - 1].id,
            },
            success: function (res) {
              if (res.data.code == 0) {
                wx.showToast({
                  title: '新增成功',
                  icon: 'success',
                  duration: 1500,
                  complete: that.modifyValue(res.data.entity, that.data.outValue, that.data.currentPath)
                })
                if (that.data.mode == "addPlan") {
                  app.globalData.addPlanTempFile[app.globalData.addPlanTempFile.length] = res.data.entity
                }
              }
            }
          })
        
      }
    },
    modifyValue: function (entity, outValue, currentPath) {
      if (entity.type == 0) {
        entity.children = []
      }
      outValue[outValue.length] = entity;
      this.setData({
        outValue: outValue,
        modalHidden: true,
        modalHidden1: true,
      })
    },
    buildData: function () {
      var that = this;
      var temp = [];
      for (var i = 0; i < this.properties.value.length; i++) {
        if (this.properties.value[i].parent == -1) {
          temp[temp.length] = this.properties.value[i];
          if (this.properties.value[i].type == 0) {
            temp[temp.length - 1].children = that.diGui(this.properties.value[i].id);
          }
        }
      }
      that.setData({
        structuredData: temp
      })
    },
    diGui: function (parent) {
      var that = this;
      var temp = [];
      for (var i = 0; i < this.properties.value.length; i++) {
        if (this.properties.value[i].parent == parent) {
          temp[temp.length] = this.properties.value[i];
          if (this.properties.value[i].type == 0) {
            temp[temp.length - 1].children = that.diGui(this.properties.value[i].id);
          }
        }
      }
      return temp;
    },

    initView() {

      if (this.properties.keepLoc) {
        if (this.data.outValue.length > 0) {
          // 记录下当前界面元素的父节点id
          let fatherId = this.properties.fatherKey;
          this.setData({
            currentFatherId: this.data.outValue[0][fatherId] || null
          });
        }
      }
      // 优先使用value
      if (this.data.structuredData.length > 0) {
        this.setData({
          normalValue: this.properties.structuredData
        });
      } else {
        // 将unnormalizedValue标准化
        this.setData({
          normalValue: this.normalizeValue()
        });
      }

      if (this.properties.keepLoc && this.data.currentFatherId !== null) {
        // 得到父元素的位置
        let fatherNodeLocation = this.searchLocById(this.data.currentFatherId);
        // 父元素的位置即是我们希望得到的当前路径
        let currentPath = fatherNodeLocation;
        this.switchPath(currentPath);
      } else {
        // 设置初始的输出值
        this.setData({
          outValue: this.data.normalValue,
          currentPath: []
        });
      }
    },
    tapItem(e) {
      // 如果正在执行修改路径的方法
      if (this.data.isChange) {
        return;
      }
      this.setData({
        isChange: true
      });
      // 获取当前点击的索引
      const currentIndex = e.currentTarget.dataset.index;
      const currentText = e.currentTarget.dataset.text;
      const currentId = e.currentTarget.dataset.id;
      // 如果当前点击的标签还有下一级，就将路径改变
      if (this.data.outValue[currentIndex].children) {
        // 添加索引如路径
        this.setData({
          currentPath: [
            ...this.data.currentPath,
            {
              text: currentText,
              index: currentIndex,
              id: currentId
            }
          ]
        });
        this.selPath();
      }
      this.setData({
        isChange: false
      });
    },
    // 选择路径
    // pathsIndex 是 paths 的索引
    selPath(pathsIndex = this.data.currentPath.length - 1) {
      // 判断是否在第一级
      if (this.data.currentPath.length === 0) {
        return;
      }
      // 根据路径修改 outValue
      let tmpValue = this.data.normalValue;
      // 如果 pathsIndex 是 -1 就应该要回到第一级
      if (pathsIndex === -1) {
        this.setData({
          currentPath: [],
          outValue: tmpValue
        });
        return;
      }
      for (let i = 0; i <= pathsIndex; i++) {
        let item = this.data.currentPath[i].index;
        tmpValue = tmpValue[item]['children'];
      }
      // 更新 outValue , currentPath
      let endIndex = pathsIndex + 1;
      this.setData({
        outValue: tmpValue,
        currentPath: this.data.currentPath.slice(0, endIndex)
      });
    },
    toPath(e) {
      // 如果正在执行修改路径的方法
      if (this.data.isChange) {
        return;
      }
      this.setData({
        isChange: true
      });
      // 获取当前点击的索引
      const index =
        e.currentTarget.dataset.index != undefined ?
        e.currentTarget.dataset.index :
        this.data.currentPath.length - 1;
      this.selPath(index - 1);
      this.setData({
        isChange: false
      });
    },
    tapBtn(e) {
      this.triggerEvent('tapBtn', e.currentTarget.dataset.item);
    },
    // 将非标准值标准化
    normalizeValue() {
      return toTree({
        value: this.properties.unnormalizedValue,
        fatherKey: this.properties.fatherKey,
        selfKey: this.properties.selfKey,
        rootValue: this.properties.rootValue === null ?
          undefined : this.properties.rootValue
      });
    },
    // 通过唯一标识符找到元素的所在位置
    // 这里特意用了loc（location）表示位置而不是path，二者的区别是loc还包括了元素自身，而path不包括元素自身
    // 如树形数据[{id:1,title:'1'}],id:1的位置是[1]，而它的路径是[]
    searchLocById(id) {
      if (id == null) {
        return null;
      }
      let tree = JSON.parse(JSON.stringify(this.data.normalValue));
      for (let i = 0; i < tree.length; i++) {
        tree[i].__location = [i];
        if (id === tree[i][this.properties.selfKey]) {
          return tree[i].__location;
        }
      }
      var stark = [];
      stark = stark.concat(tree);
      while (stark.length) {
        var temp = stark.shift();
        if (temp.children) {
          for (let j = 0; j < temp.children.length; j++) {
            temp.children[j].__location = [...temp.__location, j];
            if (id === temp.children[j][this.properties.selfKey]) {
              return temp.children[j].__location;
            }
          }
          // 当前节点有子节点时，将子节点放到当前的栈的前面
          stark = temp.children.concat(stark);
        }
      }
      // 如果找不到对应id的位置
      return null;
    },
    // 直接跳转到指定路径
    switchPath(pathArr = []) {
      let tmpValue = JSON.parse(JSON.stringify(this.data.normalValue));
      let currentPath = [];
      for (let i = 0; i < pathArr.length; i++) {
        let index = pathArr[i];
        let text = tmpValue[index][this.properties.contentKey];
        let id = tmpValue[index][this.properties.contentId];
        tmpValue = tmpValue[index]['children'];
        currentPath.push({
          text,
          index,
          id
        });
      }
      // 更新 outValue , currentPath
      this.setData({
        outValue: tmpValue,
        currentPath
      });
    }
  },

});