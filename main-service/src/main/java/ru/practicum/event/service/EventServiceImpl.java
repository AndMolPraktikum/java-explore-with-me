package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.client.MainClient;
import ru.practicum.enums.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.EventConditionForbiddenException;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.EventTimeConditionBadRequestException;
import ru.practicum.exception.EventUpdateConditionsConflictException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.service.RequestService;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final LocationRepository locationRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final CategoryService categoryService;

    private RequestService requestService;

    @Autowired
    public void setRequestService(@Lazy RequestService requestService) {
        this.requestService = requestService;
    }

    private final MainClient client;

    @Override
    public Event getEventById(long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isEmpty()) {
            log.error("Event with id={} was not found", id);
            throw new EventNotFoundException(String.format("Event with id=%d was not found", id));
        }
        return eventOptional.get();
    }

    @Override
    public List<EventFullDto> getAdminEvents(EventAdminRequestParam params) {
        List<Long> initiators = params.getUsers();
        List<State> states = params.getStates();
        List<Long> categories = params.getCategories();
        LocalDateTime rangeStart = params.getRangeStart();
        LocalDateTime rangeEnd = params.getRangeEnd();
        int from = params.getFrom();
        int size = params.getSize();
        Pageable page = PageRequest.of(from, size, Sort.by("id"));
        List<Event> eventList = eventRepository
                .findAllByInitiatorsAndStatesAndCategoriesAndTimeRange(initiators, states, categories, rangeStart,
                        rangeEnd, page);
        List<EventFullDto> eventFullDtoList = EventMapper.toEventFullDtoList(eventList);

        eventFullDtoList.forEach(event -> event.setConfirmedRequests(eventRepository
                .getCountByEventIdWhereStatusConfirmed(event.getId())));

        return client.getStatisticFull(eventFullDtoList);
    }

    @Override
    public List<EventShortDto> getPrivateUserEvents(Long userId, int from, int size) {
        userService.getUserById(userId);
        Pageable page = PageRequest.of(from, size, Sort.by("id"));
        List<Event> eventList = eventRepository.findAllByInitiatorId(userId, page);
        List<EventShortDto> result = EventMapper.toEventShortDtoList(eventList);

        result.forEach(event -> event.setConfirmedRequests(eventRepository
                .getCountByEventIdWhereStatusConfirmed(event.getId())));

        return result;
    }

    @Override
    public List<EventShortDto> getPublicEvents(EventPublicRequestParam params, String ip) {
        String text = params.getText();

        List<Long> categoryIds = params.getCategories();
        Boolean paid = params.getPaid();
        LocalDateTime rangeStart = params.getRangeStart();
        LocalDateTime rangeEnd = params.getRangeEnd();
        Boolean onlyAvailable = params.getOnlyAvailable();
        SortVariant sortVariant = params.getSortVariant();
        int from = params.getFrom();
        int size = params.getSize();

        Pageable page = PageRequest.of(from, size, Sort.by("id"));
        if (rangeStart == null & rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }
        timeCheck(rangeStart, rangeEnd);

        client.saveRegistry("/events", ip);

        List<Event> eventList = eventRepository.findAllByCriteria(text, categoryIds, paid,
                rangeStart, rangeEnd, onlyAvailable, page);

        List<EventShortDto> eventShortDtoList = EventMapper.toEventShortDtoList(eventList);

        eventShortDtoList = client.getStatisticShort(eventShortDtoList);

        if (sortVariant == SortVariant.EVENT_DATE) {
            eventShortDtoList = eventShortDtoList.stream().sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .collect(Collectors.toList());
        } else {
            eventShortDtoList = eventShortDtoList.stream().sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }

        return eventShortDtoList;
    }

    @Override
    public EventFullDto getPrivateEventById(Long userId, Long eventId) {
        userService.getUserById(userId);
        Event event = getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            log.error("The user with id={} is not the initiator of the event with id={}", userId, eventId);
            throw new EventNotFoundException(String.format("The user with id=%d is not the initiator of the " +
                    "event with id=%d", userId, eventId));
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(eventRepository.getCountByEventIdWhereStatusConfirmed(eventId));
        eventFullDto.setViews(client.getStatisticById(eventId));
        return eventFullDto;
    }

    @Override
    public EventFullDto getPublicEventById(Long id, String ip) {
        Event event = getEventById(id);
        if (!event.getState().equals(State.PUBLISHED)) {
            log.error("Event with id={} was not found", id);
            throw new EventNotFoundException(String.format("Event with id=%d was not found", id));
        }
        client.saveRegistry("/events/" + id, ip);
        EventFullDto result = EventMapper.toEventFullDto(event);
        result.setConfirmedRequests(eventRepository.getCountByEventIdWhereStatusConfirmed(id));
        result.setViews(client.getStatisticById(id));
        return result;
    }

    @Override
    public List<ParticipationRequestDto> getUserParticipation(Long userId, Long eventId) {
        getPrivateEventById(userId, eventId);
        return requestService.getUserEventParticipationRequests(eventId);
    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        timeCheck(newEventDto.getEventDate(), 2);
        User user = userService.getUserById(userId);
        Category category = categoryService.getCategoryById(newEventDto.getCategory());
        Event event = EventMapper.toEventEntity(newEventDto);
        event.setInitiator(user);
        event.setCategory(category);
        locationRepository.save(event.getLocation());
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateAdminEventById(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = updateEventConditionsCheckAdmin(eventId, updateEvent);
        updateEventFields(event, updateEvent);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updatePrivateUserEventById(Long userId, Long eventId,
                                                   UpdateEventUserRequest updateEvent) {
        userService.getUserById(userId);
        Event event = updateEventConditionsCheckUser(eventId, updateEvent);
        updateEventFields(event, updateEvent);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventStatusFromOwner(Long userId, Long eventId,
                                                                     EventRequestStatusUpdateRequest ersur) {
        if (ersur.getStatus().equals(Status.PENDING)) {
            log.error("Updated status cannot be PENDING");
            throw new EventUpdateConditionsConflictException("Updated status cannot be PENDING");
        }
        userService.getUserById(userId);
        Event event = getEventById(eventId);
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            log.error("No confirmation required");
            throw new EventUpdateConditionsConflictException("No confirmation required");
        }
        int confirmedRequest = requestService.getConfirmedRequestByEventId(eventId); //сколько заявок подтверждено
        if (confirmedRequest >= event.getParticipantLimit()) {
            log.error("Participant limit reached");
            throw new EventUpdateConditionsConflictException("Participant limit reached");
        }

        List<Long> requestIds = ersur.getRequestIds();
        List<Request> requestsToUpdateList = requestService.getAllRequestByIds(requestIds); //список заявок из запроса
        int freePlaces = event.getParticipantLimit() - confirmedRequest;
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();
        for (Request request : requestsToUpdateList) {
            if (!request.getStatus().equals(Status.PENDING)) {
                log.error("The status can be changed only for requests that are in the pending state");
                throw new EventUpdateConditionsConflictException("The status can be changed only for requests " +
                        "that are in the pending state");
            }
            if (ersur.getStatus().equals(Status.CONFIRMED) && freePlaces > 0) {
                request.setStatus(Status.CONFIRMED);
                confirmedRequests.add(request);
                freePlaces--;
            } else {
                request.setStatus(Status.REJECTED);
                rejectedRequests.add(request);
            }
        }
        requestService.updateRequestList(requestsToUpdateList);

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(RequestMapper.toRequestDtoList(confirmedRequests));
        result.setRejectedRequests(RequestMapper.toRequestDtoList(rejectedRequests));
        return result;
    }

    @Override
    public List<Event> getAllById(Set<Long> eventIds) {
        return eventRepository.findAllById(eventIds);
    }


    private void timeCheck(LocalDateTime time, int hours) {
        if (time.isBefore(LocalDateTime.now().plusHours(hours))) {
            log.error("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: {}", time);
            throw new EventTimeConditionBadRequestException(String.format("Field: eventDate. " +
                    "Error: должно содержать дату, которая еще не наступила. Value: %s", time));
        }
    }

    private void timeCheck(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            log.error("The rangeStart = {} cannot be later than the rangeEnd = {}", rangeStart, rangeEnd);
            throw new EventTimeConditionBadRequestException(String.format("The rangeStart = %s cannot be " +
                    "later than the rangeEnd = %s", rangeStart, rangeEnd));
        }
    }

    private Event updateEventConditionsCheckAdmin(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = getEventById(eventId);
        if (updateEvent.getEventDate() != null) {
            timeCheck(updateEvent.getEventDate(), 1);
            event.setEventDate(updateEvent.getEventDate());
        }

        State state = event.getState();
        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction() == StateActionAdmin.PUBLISH_EVENT) {
                if (state != State.PENDING) {
                    log.error("Cannot publish the event because it's not in the right state: {}", state);
                    throw new EventConditionForbiddenException(String.format("Cannot publish the event because it's " +
                            "not in the right state: %s", state));
                }
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (updateEvent.getStateAction() == StateActionAdmin.REJECT_EVENT) {
                if (state == State.PUBLISHED) {
                    log.error("Cannot reject the event because it's not in the right state: {}", state);
                    throw new EventConditionForbiddenException(String.format("Cannot reject the event because it's " +
                            "not in the right state: %s", state));
                }
                event.setState(State.CANCELED);
            }
        }

        return event;
    }

    private Event updateEventConditionsCheckUser(Long eventId, UpdateEventUserRequest updateEvent) {
        Event event = getEventById(eventId);
        if (event.getState() == State.PUBLISHED) {
            log.error("Only pending or canceled events can be changed");
            throw new EventConditionForbiddenException("Only pending or canceled events can be changed");
        }

        if (updateEvent.getEventDate() != null) {
            timeCheck(updateEvent.getEventDate(), 2);
            event.setEventDate(updateEvent.getEventDate());
        }

        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction() == StateActionUser.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            }
            if (updateEvent.getStateAction() == StateActionUser.SEND_TO_REVIEW) {
                event.setState(State.PENDING);
            }
        }
        return event;
    }

    private void updateEventFields(Event event, AbstractUpdateEventRequest updateEvent) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(categoryService.getCategoryById(updateEvent.getCategory()));
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getLocation() != null) {
            Location location = Location.builder()
                    .lon(updateEvent.getLocation().getLon())
                    .lat(updateEvent.getLocation().getLat())
                    .build();
            locationRepository.save(location);
            event.setLocation(location);
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
    }
}