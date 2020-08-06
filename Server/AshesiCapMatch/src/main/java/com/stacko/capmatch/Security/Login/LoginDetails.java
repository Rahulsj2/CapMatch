package com.stacko.capmatch.Security.Login;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access=AccessLevel.PROTECTED, force=true)
@AllArgsConstructor
public class LoginDetails {
	
	private String email;
	private String password;
	
}
