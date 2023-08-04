package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.enums.StateActionUser;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class UpdateEventUserRequest extends AbstractUpdateEventRequest {

    private StateActionUser stateAction;
}
