package main.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class CaptchaCodes {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, name = "id", columnDefinition = "INT")
	private int id;

	@Column(nullable = false, name = "time", columnDefinition = "DATETIME")
	private Timestamp time;

	@Column(nullable = false, name = "code", columnDefinition = "TINYTEXT")
	private String code;

	@Column(nullable = false, name = "secret_code", columnDefinition = "TINYTEXT")
	private String secretCode;

	public CaptchaCodes (int id, Timestamp time, String code, String secretCode) {
		this.id = id;
		this.time = time;
		this.code = code;
		this.secretCode = secretCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getTime() {
		return time;
}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSecretCode() {
		return secretCode;
	}

	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}
}
