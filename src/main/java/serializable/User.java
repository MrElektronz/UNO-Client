package serializable;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
@Entity
@Table(name="users")
public class User implements Serializable{

    @Id
    @Column(name="userID", unique = true)
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt", nullable = false)
    private String salt;

    public User(String username, String password, String salt){
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public int getID() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
