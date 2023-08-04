package ru.practicum.request.service;

import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;

import java.util.List;

public interface RequestService {


    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    void updateRequestList(List<Request> requestsToUpdateList);

    int getConfirmedRequestByEventId(Long eventId);

    ParticipationRequestDto cancelUpdateRequest(Long userId, Long eventId);

    List<Request> getAllRequestByIds(List<Long> requestIds);

    List<ParticipationRequestDto> getUserEventParticipationRequests(Long eventId);
}
