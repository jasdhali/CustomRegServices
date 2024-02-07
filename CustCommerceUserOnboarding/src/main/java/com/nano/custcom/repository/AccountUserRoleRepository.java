package com.nano.custcom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nano.custcom.model.AccountUserRole;

public interface AccountUserRoleRepository extends JpaRepository<AccountUserRole,Integer> {
	Optional<AccountUserRole> findByUserId(Integer userId);
}
