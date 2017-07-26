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
    time : 3000
  }, callback);
}

function showKline(code) {
  // 基于准备好的dom，初始化echarts实例
  var stockChart = echarts.init(document.getElementById('stock_charts'));
  var data = {
    categoryData : [],
    values : []
  }
  $.formAjax("#stock_form", {
    async : true,
    url : "/kline/" + code,
    method : "POST",
    success : function(result) {
      if (result.success) {
        var arr = result.data.lineData;
        var stock = result.data.stock;
        if (arr.length > 0) {
          for (var i = 0; i < arr.length; i++) {
            var obj = arr[i];
            data.categoryData.push(obj.jysj.split(" ")[0]);
            // 数据意义：开盘(open)，收盘(close)，最低(lowest)，最高(highest)
            data.values.push([ obj.kpj, obj.spj, obj.zdj, obj.zgj ]);
          }
          stockChart = renderCharts(stock.stockName, stock.code);
        } else {
          layer.open({
            content : '没有数据',
            skin : 'msg',
            time : 1
          });
        }
      }
    }
  });

  function calculateMA(dayCount) {
    var result = [];
    for (var i = 0, len = data.values.length; i < len; i++) {
      if (i < dayCount) {
        result.push('-');
        continue;
      }
      var sum = 0;
      for (var j = 0; j < dayCount; j++) {
        sum += data.values[i - j][1];
      }
      result.push(SYS.fmoney(sum / dayCount));
    }
    return result;
  }
  function renderCharts(stockName, code) {
    // data.categoryData.reverse();
    // data.values.reverse();
    var option = {
      title : {
        text : stockName + "(" + code + ")",
        left : 0
      },
      tooltip : {
        trigger : 'axis',
        axisPointer : {
          type : 'line'
        }
      },
      legend : {
        data : [ '日K', 'MA5', 'MA10', 'MA20', 'MA30', 'MA60' ]
      },
      grid : {
        left : '2%',
        right : '8%',
        bottom : '5%'
      },
      xAxis : {
        type : 'category',
        data : data.categoryData,
        scale : true,
        boundaryGap : false,
        axisLine : {
          onZero : false
        },
        splitLine : {
          show : false
        },
        splitNumber : 10,
        min : 'dataMin',
        max : 'dataMax'
      },
      yAxis : {
        scale : true,
        splitArea : {
          show : true
        }
      },
      dataZoom : [ {
        type : 'inside',
        start : 50,
        end : 100
      }, {
        show : true,
        type : 'slider',
        y : '90%',
        start : 50,
        end : 100
      } ],
      series : [ {
        name : '日K',
        type : 'candlestick',
        data : data.values,
        markPoint : {
          data : [ {
            name : 'highest value',
            type : 'max',
            valueDim : 'highest'
          }, {
            name : 'lowest value',
            type : 'min',
            valueDim : 'lowest'
          }, {
            name : 'average value on close',
            type : 'average',
            valueDim : 'close'
          } ],
          tooltip : {
            formatter : function(param) {
              return param.name + '<br>' + (param.data.coord || '');
            }
          }
        },
        markLine : {
          symbol : [ 'none', 'none' ],
          data : [ {
            name : 'min line on close',
            type : 'min',
            valueDim : 'close'
          }, {
            name : 'max line on close',
            type : 'max',
            valueDim : 'close'
          } ]
        }
      }, {
        name : 'MA5',
        type : 'line',
        data : calculateMA(5),
        smooth : true,
        lineStyle : {
          normal : {
            opacity : 0.5
          }
        }
      }, {
        name : 'MA10',
        type : 'line',
        data : calculateMA(10),
        smooth : true,
        lineStyle : {
          normal : {
            opacity : 0.5
          }
        }
      }, {
        name : 'MA20',
        type : 'line',
        data : calculateMA(20),
        smooth : true,
        lineStyle : {
          normal : {
            opacity : 0.5
          }
        }
      }, {
        name : 'MA30',
        type : 'line',
        data : calculateMA(30),
        smooth : true,
        lineStyle : {
          normal : {
            opacity : 0.5
          }
        }
      }, {
        name : 'MA60',
        type : 'line',
        data : calculateMA(60),
        smooth : true,
        lineStyle : {
          normal : {
            opacity : 0.5
          }
        }
      } ]
    };
    stockChart.setOption(option);
    return stockChart;
  }
  return stockChart;
}
function refreshKlineData(code, callback) {
  $.ajax({
    url : "/kline/refresh/" + code,
    method : "GET",
    type : "json",
    timeout : 180000,
    success : callback
  });
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
      Accept : "application/json; charset=utf-8"
    }, opts.headers || {});

    opts = jQuery.extend({
      type : "POST",
      url : "",
      data : "",
      dataType : "json",
      async : true,
      headers : headers,
      success : function() {
        return false;
      },
      fail : function() {
        return false;
      },
      complate : function() {
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
      headers : headers,
      complete : opts.complate,
      beforeSend : function() {

      },
      success : function(result) {
        // 增加session超时判断
        if (checkSessionTimeOut(result)) {
          return;
        }
        if (result.success) {
          opts.success(result); // 调用opts中callback方法
        } else {
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
      type : method,
      url : action,
      data : $(elem).serialize()
    }, opts || {}); // 逗号后面opts||{}是对前面的扩展
    $.sysAjax(opts);
  }
});