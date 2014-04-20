import org.json.simple.JSONObject;
import java.util.ArrayList;
        
public class User {
    long id;
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String token;
    String ride;
    boolean emailVerified;
    String verificationCode;
    ArrayList<Schedule> schedules;
	
    public User() {
        schedules = new ArrayList<Schedule>();
    }
    
    public static User fromJSON(JSONObject obj) throws JSONException{
	
        User user = new User();
		
        user.id = obj.getLong("Id");
        user.username = obj.getString("UserName");
        user.firstName = obj.getString("FirstName");
        user.lastName = obj.getString("LastName");
        user.email = obj.getString("Email");
        user.emailVerified = obj.getBoolean("EmailVerified");
        user.verificationCode = obj.getString("VerificationCode");
        user.ride = obj.geString("Ride");
        user.token = obj.getString("Token");
//        user.created = obj.getString("Created");
        return user;

    }

    public JSONObject toJSON(){

        JSONObject obj = new JSONObject();

        obj.put("Username", this.username);
        obj.put("Password", this.password);
        obj.put("FirstName", this.firstName);
        obj.put("LastName", this.lastName);
        obj.put("Email", this.email);
        obj.put("EmailVerified", this.emailVerified);
        obj.put("VerificationCode", this.verificationCode);
        obj.put("Ride", this.ride);
        obj.put("Token", this.token);
//	  obj.put("CreatedAt", this.created);
        return obj;
    } 
}
