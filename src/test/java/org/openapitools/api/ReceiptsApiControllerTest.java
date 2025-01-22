package org.openapitools.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Item;
import org.openapitools.model.Receipt;
import org.openapitools.model.ReceiptsIdPointsGet200Response;
import org.openapitools.model.ReceiptsProcessPost200Response;
import org.openapitools.service.ReceiptsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReceiptsApiController.class)
public class ReceiptsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReceiptsService receiptsService;

    private Receipt mockReceipt;

    @BeforeEach
    public void setUp() {
        mockReceipt = new Receipt(
                "Test Retailer",
                LocalDate.now(),
                LocalTime.now(),
                Collections.singletonList(new Item("Test Item", "1.00")),
                "10.00");
        mockReceipt.setId(UUID.fromString("3b4718ba-8f49-4f98-a738-826000101e9e"));
        mockReceipt.setScore(50L);
    }

    @Test
    public void testReceiptsIdPointsGet_ReturnsScore() throws Exception {
        String receiptId = "3b4718ba-8f49-4f98-a738-826000101e9e";
        when(receiptsService.getReceiptById(receiptId)).thenReturn(Optional.ofNullable(mockReceipt));

        ResultActions result = mockMvc.perform(get("/receipts/{id}/points", receiptId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(50));
    }

    @Test
    public void testReceiptsIdPointsGet_ReceiptNotFound() throws Exception {
        String receiptId = "3b4718ba-8f49-4f98-a738-826000101e9e";
        when(receiptsService.getReceiptById(receiptId)).thenReturn(Optional.empty());

        ResultActions result = mockMvc.perform(get("/receipts/{id}/points", receiptId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void testReceiptsProcessPost_Success() throws Exception {
        when(receiptsService.processReceipt(any(Receipt.class))).thenReturn(mockReceipt);

        ResultActions result = mockMvc.perform(post("/receipts/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockReceipt)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockReceipt.getId().toString()));
    }

    @Test
    public void testReceiptsProcessPost_BadRequest() throws Exception {
        when(receiptsService.processReceipt(any(Receipt.class))).thenReturn(null);

        ResultActions result = mockMvc.perform(post("/receipts/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockReceipt)));

        result.andExpect(status().isBadRequest());
    }
}
