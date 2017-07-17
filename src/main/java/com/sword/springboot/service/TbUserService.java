/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.sword.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.sword.springboot.mapper.TbUserMapper;
import com.sword.springboot.model.TbUser;

/**
 * @author liuzh
 * @since 2016-01-31 21:42
 */
@Service
public class TbUserService {

  @Autowired
  private TbUserMapper userMapper;

  public List<TbUser> getAll(TbUser TbUser) {
    if (TbUser.getPage() != null && TbUser.getRows() != null) {
      PageHelper.startPage(TbUser.getPage(), TbUser.getRows());
    }
    return userMapper.selectAll();
  }

  public TbUser getById(Integer id) {
    return userMapper.selectByPrimaryKey(id);
  }

  public void deleteById(Integer id) {
    userMapper.deleteByPrimaryKey(id);
  }

  public void save(TbUser bean) {
    if (bean.getId() != null) {
      userMapper.updateByPrimaryKey(bean);
    } else {
      userMapper.insert(bean);
    }
  }

  public TbUser findByUsername(String username) throws UsernameNotFoundException {
    TbUser user = findUserByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("UserName " + username + " not found");
    }
    return user;
  }

  private TbUser findUserByUsername(String username) {
    TbUser record = new TbUser();
    record.setUsername(username);
    return userMapper.selectOne(record);
  }
}
