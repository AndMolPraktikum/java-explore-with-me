package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.enums.StateActionUser;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UpdateEventUserRequest extends AbstractUpdateEventRequest {

    private StateActionUser stateAction;
}
