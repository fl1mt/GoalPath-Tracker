package tracker.goalPath.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class UserDTO {
    @JsonIgnore
    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private LocalDateTime createdAt;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
