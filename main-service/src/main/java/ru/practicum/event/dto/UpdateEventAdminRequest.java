package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.enums.StateActionAdmin;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class UpdateEventAdminRequest extends AbstractUpdateEventRequest {

    private StateActionAdmin stateAction;
}
