package main.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PostVotes {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	@OneToOne(cascade = CascadeType.ALL)
	@Column(nullable = false)
	private int userId;

	@ManyToOne(cascade = CascadeType.ALL)
	@Column(nullable = false)
	private int postId;

	@Column(nullable = false)
	private Date time;

	@Column(nullable = false)
	private int value;

	public PostVotes(int id, int userId, int postId, Date time, int value){
		this.id = id;
		this.userId = userId;
		this.postId = postId;
		this.time = time;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
