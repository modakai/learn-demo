package com.sakura.demo.multilevelcache.service;

import com.sakura.demo.multilevelcache.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        log.info("从数据库查询用户: {}", id);
        // 模拟数据库查询
        return null;
    }

    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        log.info("更新用户: {}", user);
        // 模拟数据库更新
        return user;
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        log.info("删除用户: {}", id);
        // 模拟数据库删除
    }

    @CacheEvict(value = "users", allEntries = true)
    public void clearAllUsers() {
        log.info("清除所有用户缓存");
    }
}