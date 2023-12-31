package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {

    @Autowired
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getAllCompilations(@RequestParam(defaultValue = "false") boolean pinned,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        log.info("Входящий запрос GET /compilations?pinned={}&from={}&size={}", pinned, from, size);
        List<CompilationDto> compilationDtoList = compilationService.getAllCompilations(pinned, from, size);
        log.info("Исходящий ответ: {}", compilationDtoList);
        return compilationDtoList;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Входящий запрос GET /compilations/{}", compId);
        CompilationDto compilationDto = compilationService.getCompilationDtoById(compId);
        log.info("Исходящий ответ: {}", compilationDto);
        return compilationDto;
    }
}
