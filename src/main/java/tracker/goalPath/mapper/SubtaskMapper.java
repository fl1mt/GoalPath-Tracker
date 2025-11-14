package tracker.goalPath.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tracker.goalPath.dto.SubtaskDTO;
import tracker.goalPath.model.Subtask;

@Mapper(componentModel = "spring")
public interface SubtaskMapper {
    SubtaskDTO toDTO(Subtask subtask);
    Subtask toEntity(SubtaskDTO subtaskDTO);
}
