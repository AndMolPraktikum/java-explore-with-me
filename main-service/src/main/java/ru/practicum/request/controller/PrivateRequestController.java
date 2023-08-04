package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {

    @Autowired
    private final RequestService requestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        log.info("Входящий запрос GET /users/{}/requests", userId);
        List<ParticipationRequestDto> responseList = requestService.getUserRequests(userId);
        log.info("Исходящий ответ: {}", responseList);
        return responseList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createUserRequest(@PathVariable Long userId,
                                                     @RequestParam Long eventId) {
        log.info("Входящий запрос POST /users/{}/requests?eventId={}", userId, eventId);
        ParticipationRequestDto participationRequestDto = requestService.createRequest(userId, eventId);
        log.info("Исходящий ответ: {}", participationRequestDto);
        return participationRequestDto;
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelUpdateUserRequest(@PathVariable Long userId,
                                                           @PathVariable Long requestId) {
        log.info("Входящий запрос PATCH /users/{}/requests/{}/cancel", userId, requestId);
        ParticipationRequestDto participationRequestDto = requestService.cancelUpdateRequest(userId, requestId);
        log.info("Исходящий ответ: {}", participationRequestDto);
        return participationRequestDto;
    }
}
