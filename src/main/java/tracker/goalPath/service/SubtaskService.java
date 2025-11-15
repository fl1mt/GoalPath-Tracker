package tracker.goalPath.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tracker.goalPath.dto.SubtaskDTO;
import tracker.goalPath.mapper.SubtaskMapper;
import tracker.goalPath.model.Subtask;
import tracker.goalPath.model.Task;
import tracker.goalPath.repository.SubtaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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

    public SubtaskDTO createSubtask(Long userId, Long goalId, Long taskId, SubtaskDTO subtaskDTO){

        authService.checkGoalOwnership(goalId, userId);
        Task task = authService.checkTaskOwnership(taskId, userId);

        Subtask subtask = subtaskMapper.toEntity(subtaskDTO);
        subtask.setTask(task);

        Subtask savedSubtask = subtaskRepository.save(subtask);
        return subtaskMapper.toDTO(savedSubtask);
    }

    public List<SubtaskDTO> getSubtasksByTask(Long userId, Long goalId, Long taskId) {

        authService.checkGoalOwnership(goalId, userId);
        Task task = authService.checkTaskOwnership(taskId, userId);

        List<Subtask> subtasks = subtaskRepository.findByTaskId(taskId);

        return subtasks.stream()
                .map(subtaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SubtaskDTO getSubtaskById(Long userId, Long subtaskId) {

        Subtask subtask = authService.checkSubtaskOwnership(subtaskId, userId);
        return subtaskMapper.toDTO(subtask);
    }

    public SubtaskDTO updateSubtask(Long userId, Long subtaskId, Long taskId, Long goalId, SubtaskDTO subtaskDTO) {

        authService.checkGoalOwnership(goalId, userId);
        Task task = authService.checkTaskOwnership(taskId, userId);
        Subtask existingSubtask = authService.checkSubtaskOwnership(subtaskId, userId);

        existingSubtask.setTitle(subtaskDTO.getTitle());
        existingSubtask.setDescription(subtaskDTO.getDescription());
        existingSubtask.setCompleted(subtaskDTO.getCompleted()); // bug

        Subtask updatedTask = subtaskRepository.save(existingSubtask);
        return subtaskMapper.toDTO(updatedTask);
    }

    public void deleteSubtask(Long userId, Long goalId, Long taskId, Long subtaskId) {

        authService.checkGoalOwnership(goalId, userId);
        Task task = authService.checkTaskOwnership(taskId, userId);

        Subtask subtask = authService.checkSubtaskOwnership(subtaskId, userId);

        subtaskRepository.delete(subtask);
    }
}
