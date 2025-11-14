package tracker.goalPath.dto;

public class AuthRequest {
    private String email;
    private String password;

    public void setEmail(String username){
        this.email = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

}