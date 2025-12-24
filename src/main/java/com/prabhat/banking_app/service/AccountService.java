package com.prabhat.banking_app.service;


import com.prabhat.banking_app.dto.AccountDto;
public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);
    AccountDto getAccountById(Long id);
    AccountDto deposit(Long id, double amount);


}
