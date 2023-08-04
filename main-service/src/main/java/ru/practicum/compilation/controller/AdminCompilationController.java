package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {

    @Autowired
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Входящий запрос POST /admin/compilations : {}", newCompilationDto);
        CompilationDto createdCompilation = compilationService.createCompilation(newCompilationDto);
        log.info("Исходящий ответ: {}", createdCompilation);
        return createdCompilation;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Входящий запрос DELETE /admin/compilations/{}", compId);
        compilationService.deleteCompilationById(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    private CompilationDto updateCompilation(@PathVariable Long compId,
                                             @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Входящий запрос PATCH /admin/compilations/{} : {}", compId, updateCompilationRequest);
        CompilationDto updatedCompilation = compilationService.updateCompilation(compId, updateCompilationRequest);
        log.info("Исходящий ответ: {}", updatedCompilation);
        return updatedCompilation;
    }
}
