package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.CompilationNotFoundException;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    @Autowired
    private final CompilationRepository compilationRepository;

    @Autowired
    private final EventService eventService;


    @Override
    public List<CompilationDto> getAllCompilations(boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from, size, Sort.by("id"));
        List<Compilation> compilationList;
        if (pinned) {
            compilationList = compilationRepository.findAllWherePinned(pinned, page);
        } else {
            compilationList = compilationRepository.findAll(page).getContent();
        }
        return CompilationMapper.toCompilationDtoList(compilationList);
    }

    @Override
    public CompilationDto getCompilationDtoById(Long compId) {
        return CompilationMapper.toCompilationDto(getCompilationById(compId));
    }

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilationEntity(newCompilationDto);
        List<Event> events = findEvents(newCompilationDto.getEvents());
        compilation.setEvents(events);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest update) {
        Compilation compilation = getCompilationById(compId);
        List<Event> events = findEvents(update.getEvents());

        if (!events.isEmpty()) {
            compilation.setEvents(events);
        }
        compilation.setPinned(update.isPinned());
        if (update.getTitle() != null) {
            compilation.setTitle(update.getTitle());
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompilationById(Long compId) {
        compilationRepository.deleteById(compId);
    }

    private Compilation getCompilationById(Long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        if (compilationOptional.isEmpty()) {
            log.error("Compilation with id={} was not found", compId);
            throw new CompilationNotFoundException(String.format("Compilation with id=%d was not found", compId));
        }
        return compilationOptional.get();
    }

    private List<Event> findEvents(Set<Long> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            return Collections.emptyList();
        }
        List<Event> events = eventService.getAllById(eventIds);
        if (eventIds.size() != events.size()) {
            log.error("Not all compilation events found");
            throw new CompilationNotFoundException("Not all compilation events found");
        }
        return events;
    }
}
