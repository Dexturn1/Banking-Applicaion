package com.prabhat.banking_app.controller;

import com.prabhat.banking_app.dto.AccountDto;
import com.prabhat.banking_app.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
