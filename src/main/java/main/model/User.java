package main.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	@Column(nullable = false)
	private boolean isModerator;

	@Column(nullable = false)
	private Date regTime;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	private String code;

	private String photo;

	public User(int id, boolean isModerator, Date reg_time, String name,
				String email, String password, String code, String photo) {
		this.id = id;
		this.isModerator = isModerator;
		this.regTime = reg_time;
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

	public boolean isIs_moderator() {
		return isModerator;
	}

	public void setIs_moderator(boolean is_moderator) {
		this.isModerator = is_moderator;
	}

	public Date getReg_time() {
		return regTime;
	}

	public void setReg_time(Date reg_time) {
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
