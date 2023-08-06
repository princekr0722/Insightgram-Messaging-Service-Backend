package com.Insightgram.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "access_tokens")
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty(access = Access.READ_ONLY)
	private Integer id;
	
	@Column(nullable = false, unique = true)
	@NotBlank
	private String accessToken;
	
	@Column(nullable = false)
	private LocalDateTime issuedAt = LocalDateTime.now();
	
	@Column(nullable = false)
	@NotBlank
	private String sessionJwt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	@JsonIgnore
	private UserInfo user;

	public AccessToken(String accessToken, String sessionJwt, UserInfo user) {
		this.accessToken = accessToken;
		this.user = user;
		this.sessionJwt = sessionJwt;
	}
}
