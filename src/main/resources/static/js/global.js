//general layer
function layerOpen(allVal, ele, time) {
  if (!time) {
    time = 3000;
  }
  layer.tips(allVal, ele, {
    tips : [ 2, '#3595CC' ],
    time : time
  });
}

function checkSessionTimeOut(result) { // 增加session超时判断
  if (result && !(result.success) && result.message == 'timeout') {
    top.document.location = path + '/frame.action';
    returntrue;
  }
  return false;
}
function showTip(msg, callback) {
  layer.msg(msg, {
    time: 3000
  },callback);
}
SYS = {};
/**
 * 金钱数字格式化
 */
SYS.fmoney = function(s, n) {
  n = n > 0 && n <= 20 ? n : 2; 
  s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + ""; 
  var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1]; 
  t = ""; 
  for (i = 0; i < l.length; i++) { 
  t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : ""); 
  } 
  return t.split("").reverse().join("") + "." + r; 
}

jQuery.extend({
  sysAjax : function(opts) {
    headers = jQuery.extend({
      Accept: "application/json; charset=utf-8"
    }, opts.headers || {});
    
    opts = jQuery.extend({
      type :"POST",
      url : "",
      data : "",
      dataType : "json",
      async : true,
      headers: headers,
      success : function() {
        return false;
      },
      fail : function() {
        return false;
      },
      complate : function(){
        return false;
      }
    }, opts || {}); // 逗号后面opts||{}是对前面的扩展

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });
    $.ajax({
      type : opts.type,
      dataType : opts.dataType,
      url : opts.url,
      async : opts.async,
      data : opts.data, // 多个参数用&连接
      timeout : 180000,
      headers: headers,
      complete : opts.complate,
      beforeSend: function(){
        
      },
      success : function(result) {
        // 增加session超时判断
        if (checkSessionTimeOut(result)) {
          return;
        }
        if (result.success) {
          opts.success(result); // 调用opts中callback方法
        }else{
          opts.fail(result);
        }
      },
      error : function(e) {
        if (opts.isClose) {
          closeWindows();
          showTip("操作失败");
        } else {
          opts.fail(e);
        }
      }
    });
  }
});

jQuery.extend({
  formAjax : function(elem, opts) {
    var action = $(elem).attr("action");
    var method = $(elem).attr("method");
    opts = jQuery.extend({
      type :method,
      url : action,
      data: $(elem).serialize()
    }, opts || {}); // 逗号后面opts||{}是对前面的扩展
    $.sysAjax(opts);
  }
});