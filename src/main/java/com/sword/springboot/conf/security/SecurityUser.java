package com.sword.springboot.conf.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sword.springboot.model.TbUser;

@SuppressWarnings("serial")
public class SecurityUser extends TbUser implements UserDetails {

  public SecurityUser(TbUser suser) {
    if (suser != null) {
      this.setId(suser.getId());
      this.setUsername(suser.getUsername());
      this.setEmail(suser.getEmail());
      this.setPassword(suser.getPassword());
      this.setQq(suser.getQq());
      this.setTel(suser.getTel());
      this.setUsertype(suser.getUsertype());
    }
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> authorities = new ArrayList<>();
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("admin");
    authorities.add(authority);
    return authorities;
  }

  @Override
  public String getPassword() {
    return super.getPassword();
  }

  @Override
  public String getUsername() {
    return super.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}