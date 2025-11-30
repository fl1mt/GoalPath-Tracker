package tracker.goalPath.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tracker.goalPath.dto.SubtaskDTO;
import tracker.goalPath.mapper.SubtaskMapper;
import tracker.goalPath.model.Subtask;
import tracker.goalPath.model.Task;
import tracker.goalPath.repository.SubtaskRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubtaskService {
    private final SubtaskRepository subtaskRepository;
    private final SubtaskMapper subtaskMapper;
    private final DataAuthorizationService authService;

    public SubtaskService(SubtaskRepository subtaskRepository,
                          SubtaskMapper subtaskMapper, DataAuthorizationService authService){

        this.subtaskRepository = subtaskRepository;
        this.subtaskMapper = subtaskMapper;
        this.authService = authService;
    }

    @Transactional
    public SubtaskDTO createSubtask(Long userId, UUID taskId, SubtaskDTO subtaskDTO){

        Task task = authService.checkTaskOwnership(taskId, userId);

        Subtask subtask = subtaskMapper.toEntity(subtaskDTO);
        subtask.setCompleted(false);
        subtask.setTask(task);

        Subtask savedSubtask = subtaskRepository.save(subtask);
        return subtaskMapper.toDTO(savedSubtask);
    }

    public List<SubtaskDTO> getSubtasksByTask(Long userId, UUID taskId) {

        authService.checkTaskOwnership(taskId, userId);

        List<Subtask> subtasks = subtaskRepository.findByTaskId(taskId);

        return subtasks.stream()
                .map(subtaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SubtaskDTO getSubtaskById(Long userId, UUID subtaskId) {

        Subtask subtask = authService.checkSubtaskOwnership(subtaskId, userId);
        return subtaskMapper.toDTO(subtask);
    }
    @Transactional
    public SubtaskDTO updateSubtask(Long userId, UUID subtaskId, UUID taskId, SubtaskDTO subtaskDTO) {

        authService.checkTaskOwnership(taskId, userId);
        Subtask existingSubtask = authService.checkSubtaskOwnership(subtaskId, userId);

        existingSubtask.setTitle(subtaskDTO.getTitle());
        existingSubtask.setDescription(subtaskDTO.getDescription());
        existingSubtask.setCompleted(subtaskDTO.getCompleted());

        Subtask updatedTask = subtaskRepository.save(existingSubtask);
        return subtaskMapper.toDTO(updatedTask);
    }
    @Transactional
    public void deleteSubtask(Long userId, UUID taskId, UUID subtaskId) {

        authService.checkTaskOwnership(taskId, userId);

        Subtask subtask = authService.checkSubtaskOwnership(subtaskId, userId);

        subtaskRepository.delete(subtask);
    }
}
