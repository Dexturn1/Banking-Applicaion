package com.prabhat.banking_app.service.impl;

import com.prabhat.banking_app.dto.AccountDto;
import com.prabhat.banking_app.dto.TransferFundDto;
import com.prabhat.banking_app.entity.Account;
import com.prabhat.banking_app.entity.Transaction;
import com.prabhat.banking_app.exception.AccountException;
import com.prabhat.banking_app.mapper.AccountMapper;
import com.prabhat.banking_app.repository.AccountRepository;
import com.prabhat.banking_app.repository.TransactionRepository;
import com.prabhat.banking_app.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private static final String Transaction_TYPE_DEPOSIT = "DEPOSIT";
    private static final String Transaction_TYPE_WITHDRAW = "WITHDRAW";



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

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(Transaction_TYPE_DEPOSIT);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        return new AccountDto(account.getId(), account.getAccountHolderName(),account.getBalance());
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(()->new AccountException("Account does not exist"));
        if(amount > account.getBalance())
            throw new RuntimeException("Insufficient Amount");
        account.setBalance(account.getBalance() - amount);
        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(Transaction_TYPE_WITHDRAW);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
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

    @Override
    public void transferFunds(TransferFundDto transferFundDto) {

        //  Retrieve the account from which we send the amount
        Account fromAccount = accountRepository
                .findById(transferFundDto.fromAccountId())
                .orElseThrow(()-> new AccountException("Account does not exists"));

        //  Retrieve the account to which we send the amount
        Account toAccount = accountRepository.findById(transferFundDto.toAccountId())
                .orElseThrow(()-> new AccountException("Account does not exists"));

        // Debit the amount from fromAccount object
        fromAccount.setBalance(fromAccount.getBalance() - transferFundDto.amount());

        // credit the amount to toAccount Object
        toAccount.setBalance(toAccount.getBalance() + transferFundDto.amount());

        accountRepository.save(toAccount);
        accountRepository.save(fromAccount);
    }
}
