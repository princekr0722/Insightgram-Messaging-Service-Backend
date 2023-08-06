package com.Insightgram.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.Insightgram.entity.AccessToken;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer>, PagingAndSortingRepository<AccessToken, Integer>{
	
	boolean existsByAccessToken(String accessToken);
	
	@Modifying
	@Query("DELETE FROM AccessToken WHERE accessToken = :accessToken AND sessionJwt = :sessionJwt")
	int deleteByAccessToken(String accessToken, String sessionJwt);
	
}
