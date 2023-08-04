package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {

    private Set<Long> events;       // Список id событий подборки для полной замены текущего списка

    private boolean pinned;         // Закреплена ли подборка на главной странице сайта

    @Size(min = 1, max = 50)
    private String title;           // Заголовок подборки
}
