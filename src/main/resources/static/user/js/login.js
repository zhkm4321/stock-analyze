var view_timer = null;
function viewPort(userAgent, pageWidth) {
  var oView = document.getElementById('viewport');
  if (oView) {
    document.head.removeChild(oView);
  }
  if (!pageWidth) {
    pageWidth = 640;
  }
  var screen_w = parseInt(window.screen.width), scale = screen_w / pageWidth;
  if (/Android (\d+\.\d+)/.test(userAgent)) {
    var creat_meta = document.createElement('meta');
    creat_meta.name = 'viewport';
    creat_meta.id = 'viewport';
    var version = parseFloat(RegExp.$1);
    if (version > 2.3) {
      creat_meta.content = 'width=' + pageWidth + ', initial-scale = ' + scale + ',user-scalable=1, minimum-scale = '
          + scale + ', maximum-scale = ' + scale + ', target-densitydpi=device-dpi';
    } else {
      creat_meta.content = '"width=' + pageWidth + ', target-densitydpi=device-dpi';
    }
    document.head.appendChild(creat_meta);
  } else {
    var creat_meta = document.createElement('meta');
    creat_meta.name = 'viewport';
    creat_meta.id = 'viewport';
    if (window.orientation == '-90' || window.orientation == '90') {
      scale = window.screen.height / pageWidth;
      creat_meta.content = 'width=' + pageWidth + ', initial-scale = ' + scale + ' ,minimum-scale = ' + scale
          + ', maximum-scale = ' + scale + ', user-scalable=no, target-densitydpi=device-dpi';
    } else {
      creat_meta.content = 'width=' + pageWidth + ', initial-scale = ' + scale + ' ,minimum-scale = ' + scale
          + ', maximum-scale = ' + scale + ', user-scalable=no, target-densitydpi=device-dpi';
    }
    document.head.appendChild(creat_meta);
  }
}
viewPort(navigator.userAgent);

window.onresize = function() {
  clearTimeout(view_timer);
  view_timer = setTimeout(function() {
    viewPort(navigator.userAgent);
  }, 500);
}

$(function() {
  var validForm = {
    username : false,
    password : false,
    repeatPassword : false
  }
  var allVal = '';
  // email blur
  $("input[name='username']").blur(function(allVal) {
    var emails = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    var usernameVal = $(this).val();
    if (emails.test(usernameVal)) {
      validForm.username = true;
    } else if (usernameVal == '') {
      allVal = 'Email不能为空';
      layerOpen(allVal, $(this));
      validForm.username = false;
    } else {
      allVal = '您填写的Email格式不正确';
      layerOpen(allVal, $(this));
      validForm.username = false;
    }
  });

  // password blur
  $("input[name='password']").blur(function(allVal) {
    var paswVal = $(this).val();
    if (paswVal.length == '') {
      allVal = '密码不能为空';
      layerOpen(allVal, $(this));
      validForm.password = false;
    } else {
      validForm.password = true;
    }
  });

  // password blur
  $("input[name='repeatPassword']").blur(function(allVal) {
    var repatPaswVal = $(this).val();
    var paswVal = $("input[name='password']").val();
    if (repatPaswVal !== paswVal) {
      allVal = '两次输入的密码不一致';
      layerOpen(allVal, $(this));
      validForm.repeatPassword = false;
    } else {
      validForm.repeatPassword = true;
    }
  });

  // login button
  $(".login-btn").on('click', function() {
    var paswVal = $(this).val();
    if (validForm.username && validForm.password) {
      layerOpen("登录中...", $(this), 10000);
      var _login_btn = $(this);
      $.formAjax("#login_form", {
        success : function(rs) {
          layerOpen(rs.message, _login_btn, 3000);
          window.location.href = _login_btn.attr("call-url");
        },
        fail : function(rs) {
          layerOpen(rs.message, _login_btn, 3000);
        }
      });
    }
  });

  // regist button
  $(".regist-btn").on('click', function() {
    var paswVal = $(this).val();
    if (validForm.username && validForm.password && validForm.repeatPassword) {
      var _reg_btn = $(this);
      layerOpen("注册中...", _reg_btn, 10000)
      $.formAjax("#regist_form", {
        success : function(rs) {
          layerOpen(rs.message, _reg_btn, 3000);
          window.location.href = _reg_btn.attr("call-url");
        },
        fail : function(rs) {
          layerOpen(rs.message, _reg_btn, 3000);
        }
      });
    }
  });

});