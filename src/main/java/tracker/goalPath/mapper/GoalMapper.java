package tracker.goalPath.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tracker.goalPath.dto.GoalDTO;
import tracker.goalPath.model.Goal;
import tracker.goalPath.model.User;

@Mapper(componentModel = "spring")
public interface GoalMapper {

    @Mapping(target = "userId", source = "user.id")
    GoalDTO toDTO(Goal goal);

    @Mapping(target = "updatedAt", ignore = true)
    default Goal toEntity(GoalDTO dto) {
        if (dto == null) return null;

        Goal goal = new Goal();
        goal.setTitle(dto.getTitle());
        goal.setId(dto.getId());
        goal.setDescription(dto.getDescription());
        goal.setCategory(dto.getCategory());
        goal.setDeadline(dto.getDeadline());

        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            goal.setUser(user);
        }

        return goal;
    }
}
