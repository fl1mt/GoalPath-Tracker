package tracker.goalPath.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tracker.goalPath.dto.TaskDTO;
import tracker.goalPath.model.Task;
import tracker.goalPath.model.Goal;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "goalId", source = "goal.id")
    TaskDTO toDTO(Task task);

    @Mapping(target = "updatedAt", ignore = true)
    default Task toEntity(TaskDTO dto) {
        if (dto == null) return null;

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());

        if (dto.getGoalId() != null) {
            Goal goal = new Goal();
            goal.setId(dto.getGoalId());
            task.setGoal(goal);
        }

        return task;
    }
}

