package com.nano.custcom.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nano.custcom.AuditActions;
import com.nano.custcom.dto.AppUserDTO;
import com.nano.custcom.dto.AuditLogRequestDTO;
import com.nano.custcom.model.Account;
import com.nano.custcom.model.AccountType;
import com.nano.custcom.model.AccountUserRole;
import com.nano.custcom.repository.AccountRepository;
import com.nano.custcom.repository.AccountUserRoleRepository;

@Component
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository acctRepo;
	@Autowired
	private AccountUserRoleRepository accountUserRoleRepository;

	@Override
	public List<AuditLogRequestDTO> createAccountProfile(AppUserDTO appUserDTO) {
		List<AuditLogRequestDTO> logRequestDTOs = new ArrayList<>();

		Account account = Account.builder().accountName(appUserDTO.getFirstName())
				.accountType(AccountType.Customer.name()).build();
		acctRepo.save(account);

		logRequestDTOs
				.add(AuditLogRequestDTO.builder().accountId(account.getId()).actionPerformed(AuditActions.INSERT.name())
						.artifactId(account.getId()).artifactName("ACCOUNT").build());

		AccountUserRole userRole = AccountUserRole.builder().accountId(account.getId()).userId(appUserDTO.getId())
				.build();
		logRequestDTOs
				.add(AuditLogRequestDTO.builder().accountId(account.getId()).actionPerformed(AuditActions.INSERT.name())
						.artifactId(userRole.getId()).artifactName("ACCOUNT_USER_ROLE").build());

		accountUserRoleRepository.save(userRole);
		return logRequestDTOs;
	}

}
