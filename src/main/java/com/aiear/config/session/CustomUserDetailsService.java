/**
 * 
 */
package com.aiear.config.session;


import com.aiear.config.session.User;
import com.aiear.config.session.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @packageName : com.aiear.config.session
 * @fileName	: CustomUserDetailService.java
 * @author		: pcw
 * @date		: 2022. 11. 28.
 * @description	:
 * ===============================================
 * DATE				AUTHOR			NOTE
 * 2022. 11. 28.		pcw				최초 생성
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = repository.findByUserId(userId);
        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getUserPwd(), new ArrayList<>());
//        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), new ArrayList<>());
    }
}