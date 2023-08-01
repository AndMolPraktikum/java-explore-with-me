package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enums.State;
import ru.practicum.enums.Status;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.RequestCreationConditionsException;
import ru.practicum.exception.RequestNotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    @Autowired
    private final RequestRepository requestRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private EventService eventService;


    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        userService.getUserById(userId);
        List<Request> requestList = requestRepository.findAllByRequesterId(userId);
        return RequestMapper.toRequestDtoList(requestList);
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        int confirmedRequest = getConfirmedRequestByEventId(eventId);
//        if (requestRepository.requestIsAlreadyExist(userId, eventId)) {
//            log.error("");
//        }
        if (event.getInitiator().getId().equals(userId)) {
            log.error("The initiator of the event cannot request participation in it");
            throw new RequestCreationConditionsException("The initiator of the event cannot request participation in it");
        }
        if (event.getState() != State.PUBLISHED) {
            log.error("Cannot participate in an unpublished event");
            throw new RequestCreationConditionsException("Cannot participate in an unpublished event");
        }
        if (confirmedRequest >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            log.error("Participant limit reached");
            throw new RequestCreationConditionsException("Participant limit reached");
        }
        Request request = new Request(event, user);
        if (!event.isRequestModeration() || event.getParticipantLimit() == (0)) {
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public void updateRequestList(List<Request> requestsToUpdateList) {
        requestRepository.saveAll(requestsToUpdateList);
    }

    @Override
    public int getConfirmedRequestByEventId(Long eventId) {
        return requestRepository.getCountByEventIdWhereStatusConfirmed(eventId);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelUpdateRequest(Long userId, Long requestId) {
        userService.getUserById(userId);
        Request request = getRequestById(requestId);
        request.setStatus(Status.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<Request> getAllRequestByIds(List<Long> requestIds) {
        return requestRepository.findAllById(requestIds);
    }

    @Override
    public List<ParticipationRequestDto> getUserEventParticipationRequests(Long eventId) {
        return RequestMapper.toRequestDtoList(requestRepository.findAllByEventId(eventId));
    }

    private Request getRequestById(Long requestId) {
        Optional<Request> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isEmpty()) {
            log.error("Request with id={} was not found", requestId);
            throw new RequestNotFoundException(String.format("Request with id=%d was not found", requestId));
        }
        return requestOptional.get();
    }
}
