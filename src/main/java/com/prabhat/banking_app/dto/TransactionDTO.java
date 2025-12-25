package com.prabhat.banking_app.dto;

import java.time.LocalDateTime;

public record TransactionDTO(Long id,
                             Long accountId,
                             double amount,
                             String transactionType,
                             LocalDateTime timestamps) {

}