package com.nano.custcom.service;

import java.util.List;

import com.nano.custcom.dto.AppUserDTO;
import com.nano.custcom.dto.AuditLogRequestDTO;

public interface AccountService {
	List<AuditLogRequestDTO> createAccountProfile(AppUserDTO appUserDTO);
}
