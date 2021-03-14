package main.model;

import javax.persistence.*;

@Entity
public class Tags {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, name = "id", columnDefinition = "INT")
	private int id;

	@Column(nullable = false, name = "name", columnDefinition = "VARCHAR(255)")
	private String name;

	public Tags(int id, String name){
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
