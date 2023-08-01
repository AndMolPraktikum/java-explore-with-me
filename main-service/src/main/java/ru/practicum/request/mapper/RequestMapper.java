package ru.practicum.request.mapper;

import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {

    public static ParticipationRequestDto toRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }

    public static List<ParticipationRequestDto> toRequestDtoList(List<Request> requestList) {
        return requestList.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }
}
