package com.prabhat.banking_app.service.impl;

import com.prabhat.banking_app.dto.AccountDto;
import com.prabhat.banking_app.entity.Account;
import com.prabhat.banking_app.mapper.AccountMapper;
import com.prabhat.banking_app.repository.AccountRepository;
import com.prabhat.banking_app.service.AccountService;
import org.springframework.stereotype.Service;


@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return new AccountDto(savedAccount.getId(),
                savedAccount.getAccountHolderName(),
                savedAccount.getBalance());
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()->new RuntimeException("Account does not exist"));
        return new AccountDto(account.getId(), account.getAccountHolderName(), account.getBalance());
    }

    @Override
    public AccountDto deposit(Long id, double amount) {

        Account account = accountRepository.findById(id).orElseThrow(()->new RuntimeException("Account does not exist"));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        return new AccountDto(account.getId(), account.getAccountHolderName(),account.getBalance());
    }
}
