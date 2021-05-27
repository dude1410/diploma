package main.model;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Transactional
@Entity
@Table(name = "captcha_codes")
public class CaptchaCodes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "id", columnDefinition = "INT")
	private int id;

	@Column(name = "time", columnDefinition = "DATETIME")
	private Timestamp time;

	@Column(nullable = false, name = "code", columnDefinition = "TEXT")
	private String code;

	@Column(name = "secret_code", columnDefinition = "TEXT")
	private String secretCode;

	public CaptchaCodes (int id, Timestamp time, String code, String secretCode) {
		this.id = id;
		this.time = time;
		this.code = code;
		this.secretCode = secretCode;
	}

	public CaptchaCodes() {
	}

    public CaptchaCodes(Date date, String toString, String toString1) {
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
