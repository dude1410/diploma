package main.model;

import javax.persistence.*;

@Entity
public class GlobalSettings {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, name = "id", columnDefinition = "INT")
	private int id;

	@Column(nullable = false, name = "code", columnDefinition = "VARCHAR(255)")
	private String code;

	@Column(nullable = false, name = "name", columnDefinition = "VARCHAR(255)")
	private String name;

	@Column(nullable = false, name = "value", columnDefinition = "VARCHAR(255)")
	private String value;

	public GlobalSettings (int id, String code, String name, String value) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
