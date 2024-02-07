package com.nano.custcom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nano.custcom.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{

}
