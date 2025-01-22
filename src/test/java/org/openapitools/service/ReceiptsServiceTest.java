package org.openapitools.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openapitools.model.Item;
import org.openapitools.model.Receipt;
import org.openapitools.repository.ReceiptsRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReceiptsServiceTest {

    @Mock
    private ReceiptsRepository receiptsRepository;

    @InjectMocks
    private ReceiptsService receiptsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessReceipt() {
        UUID receiptId = UUID.randomUUID();
        Receipt receipt = new Receipt(
                "Test Retailer",
                LocalDate.now(),
                LocalTime.now(),
                Collections.singletonList(new Item("Test Item", "1.00")),
                "10.00"
        );

        Receipt savedReceipt = new Receipt(
                "Test Retailer",
                LocalDate.now(),
                LocalTime.now(),
                Collections.singletonList(new Item("Test Item", "1.00")),
                "10.00"
        );
        savedReceipt.setId(receiptId);

        when(receiptsRepository.save(any(Receipt.class))).thenReturn(savedReceipt);

        Receipt result = receiptsService.processReceipt(receipt);

        assertNotNull(result);
        assertEquals(savedReceipt.getId(), result.getId());
        verify(receiptsRepository, times(1)).save(receipt);
    }

    @Test
    void testProcessReceipt_ItemsHaveRelationshipSet() {
        Receipt receipt = new Receipt(
                "Test Retailer",
                LocalDate.now(),
                LocalTime.now(),
                Collections.singletonList(new Item("Test Item", "1.00")),
                "10.00"
        );

        when(receiptsRepository.save(any(Receipt.class))).thenReturn(receipt);

        Receipt result = receiptsService.processReceipt(receipt);

        assertNotNull(result);
        assertEquals(receipt, result.getItems().get(0).getReceipt());
    }

    @Test
    void testGetReceiptById_ExistingId() {
        UUID receiptId = UUID.randomUUID();
        Receipt receipt = new Receipt(
                "Test Retailer",
                LocalDate.now(),
                LocalTime.now(),
                Collections.singletonList(new Item("Test Item", "1.00")),
                "10.00"
        );
        receipt.setId(receiptId);

        when(receiptsRepository.findById(receiptId)).thenReturn(Optional.of(receipt));

        Optional<Receipt> result = receiptsService.getReceiptById(receiptId.toString());

        assertNotNull(result.get());
        assertEquals(receiptId, result.get().getId());
        verify(receiptsRepository, times(1)).findById(receiptId);
    }

    @Test
    void testGetReceiptById_NonExistingId() {
        UUID receiptId = UUID.randomUUID();
        when(receiptsRepository.findById(receiptId)).thenReturn(Optional.empty());

        Optional<Receipt> result = receiptsService.getReceiptById(receiptId.toString());

        assertFalse(result.isPresent());
        verify(receiptsRepository, times(1)).findById(receiptId);
    }
}
