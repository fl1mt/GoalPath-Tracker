package tracker.goalPath.service;

import org.springframework.stereotype.Service;
import tracker.goalPath.dto.UserDTO;
import tracker.goalPath.mapper.UserMapper;
import tracker.goalPath.model.User;
import tracker.goalPath.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO getUserProfile(User user){
        return userMapper.toDTO(user);
    }

    public UserDTO updateUserUsername(User userToUpdate, UserDTO userDTO) {
        if (userDTO != null && userDTO.getUsername() != null && !userDTO.getUsername().trim().isEmpty()) {
            userToUpdate.setUsername(userDTO.getUsername());
            userRepository.save(userToUpdate);
        }

        return userMapper.toDTO(userToUpdate);
    }
}
