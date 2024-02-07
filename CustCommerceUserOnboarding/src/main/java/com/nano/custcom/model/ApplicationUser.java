package com.nano.custcom.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
//@Table(name = "user")
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ApplicationUser {
	// @Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String firstName;
	private String secondName;
	private String email;
	private String password;
	private String passwordStr;

	@Enumerated(EnumType.STRING)
	private AppUserRole role;

}
