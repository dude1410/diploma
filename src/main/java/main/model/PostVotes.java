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
	private User user;

	@ManyToOne(cascade = CascadeType.ALL)
	@Column(nullable = false)
	private Post post;

	@Column(nullable = false)
	private Date time;

	@Column(nullable = false)
	private int value;

	public PostVotes(int id, User user, Post post, Date time, int value){
		this.id = id;
		this.user = user;
		this.post = post;
		this.time = time;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Post getPost() {
		return post;
	}

	public void setPostId(Post post) {
		this.post = post;
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
