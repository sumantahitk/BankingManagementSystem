package org.banking.service;

import lombok.RequiredArgsConstructor;
import org.banking.dto.response.PagedResponse;
import org.banking.dto.response.TransactionResponse;
import org.banking.entity.Transaction;
import org.banking.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public PagedResponse<TransactionResponse> getAccountTransactions(Long accountNumber, org.banking.entity.TransactionType type, int page, int size) {
        Page<Transaction> ts;
        if (type != null) {
            ts = transactionRepository.findByAccountAccountNumberAndType(accountNumber, type, PageRequest.of(page, size));
        } else {
            ts = transactionRepository.findByAccountAccountNumber(accountNumber, PageRequest.of(page, size));
        }
        return mapToPagedResponse(ts);
    }

    @Override
    public PagedResponse<TransactionResponse> getAllTransactions(int page, int size) {
        Page<Transaction> ts = transactionRepository.findAll(PageRequest.of(page, size));
        return mapToPagedResponse(ts);
    }

    private PagedResponse<TransactionResponse> mapToPagedResponse(Page<Transaction> ts) {
        return PagedResponse.<TransactionResponse>builder()
                .content(ts.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .page(ts.getNumber()).size(ts.getSize()).totalElements(ts.getTotalElements()).totalPages(ts.getTotalPages()).last(ts.isLast()).build();
    }

    private TransactionResponse mapToResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .type(t.getType())
                .amount(t.getAmount())
                .timestamp(t.getTimestamp())
                .status(t.getStatus())
                .description(t.getDescription())
                .referenceNumber(t.getReferenceNumber())
                .accountNumber(t.getAccount().getAccountNumber())
                .customerId(t.getAccount().getCustomer().getId())
                .senderName(t.getSenderName())
                .receiverName(t.getReceiverName())
                .build();
    }
}
