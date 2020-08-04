package com.stacko.capmatch.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class RequestError {
	
	@Getter
	@Setter
	private String message;
}
