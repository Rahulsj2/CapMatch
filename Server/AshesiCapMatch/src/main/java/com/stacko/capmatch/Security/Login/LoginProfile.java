package com.stacko.capmatch.Security.Login;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.stacko.capmatch.Models.User;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Entity
@Data
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@RequiredArgsConstructor
public class LoginProfile {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int loginId;
	
	@OneToOne(cascade= CascadeType.PERSIST, optional=false)
    @JoinColumn(name="fk_user", unique=true)
	private final User user;
	
	private int failedAttempts;
	
	private Date lastLogin;
	
	private Date lastFailedLogin;
	
}