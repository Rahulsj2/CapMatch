package com.stacko.capmatch.Security.Signup;


import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

import com.stacko.capmatch.Models.User;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@Entity
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
public class AccountConfirmation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int confirmationId;

	@Column(nullable=false, unique=true)
	private final String confirmCode;
	
	@OneToOne(cascade= CascadeType.PERSIST, optional=false)
    @JoinColumn(name="fk_user")
	private final User user;
	
	private Date dateCreated;
	
	
	@PrePersist
	private void preSaveSetup() {
		this.dateCreated = new Date();
	}
}
