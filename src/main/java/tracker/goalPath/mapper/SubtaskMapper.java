package tracker.goalPath.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tracker.goalPath.dto.SubtaskDTO;
import tracker.goalPath.model.Subtask;
import tracker.goalPath.model.Task;

@Mapper(componentModel = "spring")
public interface SubtaskMapper {

    @Mapping(target = "taskId", source = "task.id")
    SubtaskDTO toDTO(Subtask subtask);

    @Mapping(target = "updatedAt", ignore = true)
    default Subtask toEntity(SubtaskDTO dto) {

        if (dto == null) {
            return null;
        }

        Subtask subtask = new Subtask();
        subtask.setTitle(dto.getTitle());
        subtask.setId(dto.getId());
        subtask.setDescription(dto.getDescription());
        subtask.setCompleted(dto.getCompleted());

        if (dto.getTaskId() != null) {
            Task task = new Task();
            task.setId(dto.getTaskId());
            subtask.setTask(task);
        }

        return subtask;
    }
}
