package ru.practicum.event.mapper;

import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.enums.State;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {

    public static Event toEventEntity(NewEventDto newEventDto) {
        return new Event(
                newEventDto.getAnnotation(),
                newEventDto.getDescription(),
                newEventDto.getEventDate(),
                EventMapper.toLocationEntity(newEventDto.getLocation()),
                newEventDto.isPaid(),
                newEventDto.getParticipantLimit(),
                newEventDto.isRequestModeration(),
                State.PENDING,
                newEventDto.getTitle()
        );
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(EventMapper.toLocationDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getEventDate(),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle()
        );
    }

    public static List<EventFullDto> toEventFullDtoList(List<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public static List<EventShortDto> toEventShortDtoList(List<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    private static Location toLocationEntity(LocationDto locationDto) {
        return new Location(
                locationDto.getLat(),
                locationDto.getLon()
        );
    }

    private static LocationDto toLocationDto(Location location) {
        return new LocationDto(
                location.getLat(),
                location.getLon()
        );
    }
}
