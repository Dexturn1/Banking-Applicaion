package com.prabhat.banking_app.controller;

import com.prabhat.banking_app.dto.AccountDto;
import com.prabhat.banking_app.dto.TransactionDTO;
import com.prabhat.banking_app.dto.TransferFundDto;
import com.prabhat.banking_app.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {

        private final AccountService accountService;

        // Add account rest API
        @PostMapping()
        public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto){
            return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
        }


        // retrieve the account by id
        @GetMapping("/{id}")
        public ResponseEntity<AccountDto> getAccountByID(@PathVariable(name = "id") Long id){
                AccountDto accountDto = accountService.getAccountById(id);
                return new ResponseEntity<>(accountDto,HttpStatus.OK);
        }

        // Deposit REST API
        @PutMapping("/{id}/deposit")
        public ResponseEntity<AccountDto> deposit(@PathVariable (name = "id") Long id, @RequestBody Map<String, Double> request ){
                Double amount = request.get("amount");
                AccountDto accountDto = accountService.deposit(id, request.get("amount"));
                return new ResponseEntity<>(accountDto, HttpStatus.OK);
        }

        @PutMapping("/{id}/withdraw")
        public ResponseEntity<AccountDto> withdraw(@PathVariable (name = "id") Long id,@RequestBody Map<String, Double> request){
                Double amount = request.get("amount");
                AccountDto accountDto = accountService.withdraw(id, request.get("amount"));
                return new ResponseEntity<>(accountDto, HttpStatus.OK);
        }

        @GetMapping()
        public ResponseEntity<List<AccountDto>> getAllAccount(){
                List<AccountDto> list = accountService.getAllAccounts();
                return new ResponseEntity<>(list, HttpStatus.OK);
        }


        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteById(@PathVariable Long id){
                accountService.deleteAccount(id);
                return new ResponseEntity<>("Account deleted Successfully!", HttpStatus.OK);
        }

        @PostMapping("/transfer")
        public ResponseEntity<String> transferFunds(@RequestBody TransferFundDto transferFundDto){
                accountService.transferFunds(transferFundDto);
                return new ResponseEntity<>("Transfer successfully!", HttpStatus.OK);
        }

        // Build transaction REST API
        @GetMapping("/{id}/trancactions")
        public ResponseEntity<List<TransactionDTO>> fetchTransactions(@PathVariable("id") Long accountID){

                List<TransactionDTO> transactions =accountService.getAccountTransactions(accountID);

                return new ResponseEntity<>(transactions, HttpStatus.OK);
        }
}
