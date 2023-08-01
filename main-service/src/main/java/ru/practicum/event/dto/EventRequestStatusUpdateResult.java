package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {   // Результат подтверждения/отклонения заявок на участие в событии

    private List<ParticipationRequestDto> confirmedRequests;

    private List<ParticipationRequestDto> rejectedRequests;
}
