package com.example.bankcards.controller;

import com.example.bankcards.config.SecurityConfig;
import com.example.bankcards.dto.card.BlockingCardRequestDto;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.TransferBetweenCardsRequest;
import com.example.bankcards.entity.request.BlockingCardStatus;
import com.example.bankcards.service.auth.JwtTokenService;
import com.example.bankcards.service.card.CardService;
import com.example.bankcards.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserCardController.class)
@Import(SecurityConfig.class)
@WithMockUser
class UserCardControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private CardService cardService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtTokenService jwtTokenService;

    private final CardDto cardDto = CardDto.builder()
            .username("username")
            .maskedCardNumber("**** **** **** 1111")
            .expirationDate(LocalDate.now().plusYears(10))
            .balance(BigDecimal.valueOf(0.0))
            .id(UUID.randomUUID())
            .build();
    private final BlockingCardRequestDto blockingCardRequestDto = BlockingCardRequestDto.builder()
            .cardId(UUID.randomUUID())
            .id(UUID.randomUUID())
            .initiatorId(UUID.randomUUID())
            .state(BlockingCardStatus.WAITING)
            .build();

    @Test
    void testGetAllCardsByUser() throws Exception {
        when(cardService.getAllCardsByUser(any(UUID.class), any(), any())).thenReturn(List.of(cardDto));

        mvc.perform(get("/users/cards")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDto.getId()))
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(cardService, times(1)).getAllCardsByUser(any(UUID.class), any(), any());
    }

    @Test
    void testAddBlockingCardRequest() throws Exception {
        when(cardService.addBlockingCardRequest(any(UUID.class))).thenReturn(blockingCardRequestDto);

        mvc.perform(post("/users/cards/blocking")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockingCardRequestDto.getCardId())))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(blockingCardRequestDto.getId().toString())))
                .andExpect(jsonPath("$.cardId", is(blockingCardRequestDto.getCardId().toString())))
                .andExpect(jsonPath("$.initiatorId", is(blockingCardRequestDto.getInitiatorId().toString())))
                .andExpect(jsonPath("$.state", is(blockingCardRequestDto.getState().toString())));

        verify(cardService, times(1)).addBlockingCardRequest(any(UUID.class));
    }

    @Test
    void testTransferBetweenCards() throws Exception {
        mvc.perform(post("/users/cards/transfer")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TransferBetweenCardsRequest(UUID.randomUUID(),
                                UUID.randomUUID(), BigDecimal.valueOf(1.0))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        verify(cardService, times(1)).transferBetweenCards(any(TransferBetweenCardsRequest.class));
    }

    @Test
    void getCardBalance() throws Exception {
        when(cardService.getBalance(any(UUID.class))).thenReturn(BigDecimal.valueOf(0.0));

        mvc.perform(get("/users/cards/balance")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UUID.randomUUID()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(BigDecimal.valueOf(0.0)), BigDecimal.class));

        verify(cardService, times(1)).getBalance(any(UUID.class));
    }
}