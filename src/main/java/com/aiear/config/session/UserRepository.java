/**
 * 
 */
package com.aiear.config.session;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @packageName : com.aiear.config.session
 * @fileName	: UserRepostitory.java
 * @author		: pcw
 * @date		: 2022. 11. 28.
 * @description	:
 * ===============================================
 * DATE				AUTHOR			NOTE
 * 2022. 11. 28.		pcw				최초 생성
 */
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUserId(String userId);
}