package com.example.bankcards.controller;

import com.example.bankcards.config.SecurityConfig;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.CreateCardDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminCardController.class)
@Import(SecurityConfig.class)
class AdminCardControllerTest {
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
            .id(UUID.randomUUID())
            .balance(BigDecimal.valueOf(0.0))
            .expirationDate(LocalDate.now().plusYears(10))
            .maskedCardNumber("**** **** **** 1111")
            .username("username")
            .build();

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateCard() throws Exception {
        when(cardService.createCard(any(CreateCardDto.class))).thenReturn(cardDto);

        mvc.perform(post("/v1/admin/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(CreateCardDto.builder()
                                .cardNumber("4111111111111111")
                                .userId(UUID.randomUUID())
                                .build()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(cardDto.getId().toString())))
                .andExpect(jsonPath("$.balance", is(cardDto.getBalance()), BigDecimal.class))
                .andExpect(jsonPath("$.maskedCardNumber", is(cardDto.getMaskedCardNumber())))
                .andExpect(jsonPath("$.expirationDate", is(cardDto.getExpirationDate().toString())))
                .andExpect(jsonPath("$.username", is(cardDto.getUsername())));

        verify(cardService, times(1)).createCard(any(CreateCardDto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testBlockingCard() throws Exception {
        mvc.perform(patch("/v1/admin/cards/{cardId}/blocking", UUID.randomUUID()))
                .andExpect(status().isOk());

        verify(cardService, times(1)).blockingCard(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testBlockingCardByRequest() throws Exception {
        mvc.perform(patch("/v1/admin/cards/{requestId}/blocking/byRequest", UUID.randomUUID()))
                .andExpect(status().isOk());

        verify(cardService, times(1)).blockingCardByRequest(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testActivateCard() throws Exception {
        mvc.perform(patch("/v1/admin/cards/{cardId}/activate", UUID.randomUUID()))
                .andExpect(status().isOk());

        verify(cardService, times(1)).activateCard(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteCard() throws Exception {
        mvc.perform(delete("/v1/admin/cards/{cardId}", UUID.randomUUID()))
                .andExpect(status().isOk());

        verify(cardService, times(1)).deleteCard(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetCardById() throws Exception {
        when(cardService.getCardById(any(UUID.class))).thenReturn(cardDto);

        mvc.perform(get("/v1/admin/cards/{cardId}", cardDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(cardDto.getId().toString())))
                .andExpect(jsonPath("$.maskedCardNumber", is(cardDto.getMaskedCardNumber())))
                .andExpect(jsonPath("$.username", is(cardDto.getUsername())))
                .andExpect(jsonPath("$.expirationDate", is(cardDto.getExpirationDate().toString())))
                .andExpect(jsonPath("$.balance", is(cardDto.getBalance()), BigDecimal.class));

        verify(cardService, times(1)).getCardById(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllCards() throws Exception {
        when(cardService.getAllCards()).thenReturn(List.of(cardDto));

        mvc.perform(get("/v1/admin/cards")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(cardService, times(1)).getAllCards();
    }
}