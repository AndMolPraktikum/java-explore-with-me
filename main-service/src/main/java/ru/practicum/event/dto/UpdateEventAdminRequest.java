package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.enums.StateActionAdmin;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UpdateEventAdminRequest extends AbstractUpdateEventRequest {

    private StateActionAdmin stateAction;
}
