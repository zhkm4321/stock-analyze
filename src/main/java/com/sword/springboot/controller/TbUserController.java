
package com.sword.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;
import com.sword.springboot.model.TbUser;
import com.sword.springboot.service.TbUserService;

/**
 * @author liuzh
 * @since 2015-12-19 11:10
 */
@RestController
@RequestMapping("/users")
public class TbUserController {

  @Autowired
  private TbUserService userSvc;

  @RequestMapping
  public PageInfo<TbUser> getAll(TbUser userInfo) {
    List<TbUser> userInfoList = userSvc.getAll(userInfo);
    return new PageInfo<TbUser>(userInfoList);
  }

  @RequestMapping(value = "/add")
  public TbUser add() {
    return new TbUser();
  }

  @RequestMapping(value = "/view/{id}")
  public TbUser view(@PathVariable Integer id) {
    ModelAndView result = new ModelAndView();
    TbUser userInfo = userSvc.getById(id);
    return userInfo;
  }

  @RequestMapping(value = "/delete/{id}")
  public ModelMap delete(@PathVariable Integer id) {
    ModelMap result = new ModelMap();
    userSvc.deleteById(id);
    result.put("msg", "删除成功!");
    return result;
  }

  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public ModelMap save(TbUser userInfo) {
    ModelMap result = new ModelMap();
    String msg = userInfo.getId() == null ? "新增成功!" : "更新成功!";
    userSvc.save(userInfo);
    result.put("userInfo", userInfo);
    result.put("msg", msg);
    return result;
  }
}
