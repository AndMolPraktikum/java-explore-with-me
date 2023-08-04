package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;
import java.util.Set;

public interface EventService {

    Event getEventById(long id);

    List<EventFullDto> getAdminEvents(EventAdminRequestParam params);

    List<EventShortDto> getPrivateUserEvents(Long userId, int from, int size);

    List<EventShortDto> getPublicEvents(EventPublicRequestParam params, String ip);

    EventFullDto getPrivateEventById(Long userId, Long eventId);

    EventFullDto getPublicEventById(Long id, String ip);

    List<ParticipationRequestDto> getUserParticipation(Long userId, Long eventId);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto updateAdminEventById(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    EventFullDto updatePrivateUserEventById(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventRequestStatusUpdateResult updateEventStatusFromOwner(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<Event> getAllById(Set<Long> events);
}
