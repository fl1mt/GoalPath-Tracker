package tracker.goalPath.dto;

public class AuthResponse {
    private String token;
    //private String username;
    //private String email;
    public AuthResponse(String token)
    {
        this.token = token;
    }
    //public void setUsername(String username){
    //    this.username = username;
    //}

    //public void setEmail(String email){
    //    this.email = email;
    //}

    //public String getEmail(){
      //  return email;
    //}

    //public String getUsername(){
     //   return username;
    //}

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}