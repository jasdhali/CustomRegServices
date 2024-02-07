package com.nano.custcom.dto;

import com.nano.custcom.model.AppUserRole;
import com.nano.custcom.model.ApplicationUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AppUserDTO {
	private Integer id;	
	private String firstName;
	private String secondName;
	private String email;
	private String password;
	private AppUserRole role;
	
	public static AppUserDTO from(ApplicationUser user) {
		return AppUserDTO
				.builder()
				.firstName(user.getFirstName())
				.secondName( user.getSecondName())
				.password(user.getPasswordStr())
				.build();
	}	
}
