package com.Insightgram.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Insightgram.entity.UserInfo;


public interface UserInfoRepository extends JpaRepository<UserInfo, Integer>{
	
	Optional<UserInfo> findByUsername(String username);
	boolean existsByUsername(String username);
	
}
