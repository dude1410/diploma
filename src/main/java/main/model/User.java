package main.model;

import main.security.Role;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "id", columnDefinition = "INT")
	private int id;

	@Column(nullable = false, name = "is_moderator", columnDefinition = "INT")
	private int isModerator;

	@Column(nullable = false, name = "reg_time", columnDefinition = "DATETIME")
	private Timestamp regTime;

	@Column(nullable = false, name = "name", columnDefinition = "VARCHAR(255)")
	private String name;

	@Column(nullable = false, name = "email", columnDefinition = "VARCHAR(255)")
	private String email;

	@Column(nullable = false, name = "password", columnDefinition = "VARCHAR(255)")
	private String password;

	@Column(name = "code", columnDefinition = "TEXT")
	private String code;

	@Column(name = "photo", columnDefinition = "TEXT")
	private String photo;

	public Role getRole(){
		return isModerator == 1 ? Role.MODERATOR : Role.USER;
	}

	public User(){};

	public User(int id, byte isModerator, Timestamp regTime, String name,
				String email, String password, String code, String photo) {
		this.id = id;
		this.isModerator = isModerator;
		this.regTime = regTime;
		this.name = name;
		this.email = email;
		this.password = password;
		this.code = code;
		this.photo = photo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int isIs_moderator() {
		return isModerator;
	}

	public void setIs_moderator(int is_moderator) {
		this.isModerator = is_moderator;
	}

	public Timestamp getReg_time() {
		return regTime;
	}

	public void setReg_time(Timestamp reg_time) {
		this.regTime = reg_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
}
