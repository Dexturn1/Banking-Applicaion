package com.prabhat.banking_app.service.impl;

import com.prabhat.banking_app.dto.AccountDto;
import com.prabhat.banking_app.dto.TransactionDTO;
import com.prabhat.banking_app.dto.TransferFundDto;
import com.prabhat.banking_app.entity.Account;
import com.prabhat.banking_app.entity.Transaction;
import com.prabhat.banking_app.exception.AccountException;
import com.prabhat.banking_app.mapper.AccountMapper;
import com.prabhat.banking_app.repository.AccountRepository;
import com.prabhat.banking_app.repository.TransactionRepository;
import com.prabhat.banking_app.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private static final String Transaction_TYPE_DEPOSIT = "DEPOSIT";
    private static final String Transaction_TYPE_WITHDRAW = "WITHDRAW";
    private static final String Transaction_TYPE_TRANSFER = "TRANSFER";



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

    @Transactional
    @Override
    public void transferFunds(TransferFundDto transferFundDto) {

        Account fromAccount = accountRepository
                .findById(transferFundDto.fromAccountId())
                .orElseThrow(() -> new AccountException("Sender account does not exist"));

        Account toAccount = accountRepository
                .findById(transferFundDto.toAccountId())
                .orElseThrow(() -> new AccountException("Receiver account does not exist"));

        if (transferFundDto.amount() <= 0) {
            throw new AccountException("Amount must be greater than zero");
        }

        if (fromAccount.getBalance() < transferFundDto.amount()) {
            throw new AccountException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - transferFundDto.amount());
        toAccount.setBalance(toAccount.getBalance() + transferFundDto.amount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction debitTxn = new Transaction();
        debitTxn.setAccountId(fromAccount.getId());
        debitTxn.setAmount(transferFundDto.amount());
        debitTxn.setTransactionType("TRANSFER_DEBIT");
        debitTxn.setTimestamp(LocalDateTime.now());

        Transaction creditTxn = new Transaction();
        creditTxn.setAccountId(toAccount.getId());
        creditTxn.setAmount(transferFundDto.amount());
        creditTxn.setTransactionType("TRANSFER_CREDIT");
        creditTxn.setTimestamp(LocalDateTime.now());

        transactionRepository.save(debitTxn);
        transactionRepository.save(creditTxn);
    }

    @Override
    public List<TransactionDTO> getAccountTransactions(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
        return transactions.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private TransactionDTO  convertEntityToDto(Transaction transaction){
        TransactionDTO transactionDto =
                new TransactionDTO(transaction.getId()
                        ,transaction.getAccountId(),
                        transaction.getAmount(),
                        transaction.getTransactionType(),
                        transaction.getTimestamp());
        return transactionDto;
    }


}
