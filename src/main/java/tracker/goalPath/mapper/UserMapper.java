package tracker.goalPath.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tracker.goalPath.dto.UserDTO;
import tracker.goalPath.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO dto);
}
