package com.stacko.capmatch.Security.Signup;

import com.stacko.capmatch.Models.Interest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interests {
	Iterable<Interest> interests;
}
