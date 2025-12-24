package com.prabhat.banking_app.service.impl;

import com.prabhat.banking_app.dto.AccountDto;
import com.prabhat.banking_app.entity.Account;
import com.prabhat.banking_app.exception.AccountException;
import com.prabhat.banking_app.mapper.AccountMapper;
import com.prabhat.banking_app.repository.AccountRepository;
import com.prabhat.banking_app.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;


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
        Account account = accountRepository.findById(id).orElseThrow(()->new AccountException("Account does not exist"));
        return new AccountDto(account.getId(), account.getAccountHolderName(), account.getBalance());
    }

    @Override
    public AccountDto deposit(Long id, double amount) {

        Account account = accountRepository.findById(id).orElseThrow(()->new AccountException("Account does not exist"));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        return new AccountDto(account.getId(), account.getAccountHolderName(),account.getBalance());
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(()->new AccountException("Account does not exist"));

        if(amount > account.getBalance())
            throw new RuntimeException("Insufficient Amount");

        account.setBalance(account.getBalance() - amount);
        Account savedAccount = accountRepository.save(account);
        return new AccountDto(savedAccount.getId(), savedAccount.getAccountHolderName(), savedAccount.getBalance());
    }

    @Override
    public List<AccountDto> getAllAccounts() {

        List<Account> listOfAccount = accountRepository.findAll();

        return listOfAccount.stream().map(AccountMapper::mapToAccountDto).toList();
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()-> new AccountException("Account does not exist"));
        accountRepository.delete(account);
    }
}
