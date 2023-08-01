package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.mapper.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation toCompilationEntity(NewCompilationDto newCompilationDto) {
        return new Compilation(
                newCompilationDto.isPinned(),
                newCompilationDto.getTitle()
        );
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList()),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }

    public static List<CompilationDto> toCompilationDtoList(List<Compilation> compilationList) {
        return compilationList.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}
