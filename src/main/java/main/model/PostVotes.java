package main.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class PostVotes {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, name = "id", columnDefinition = "INT")
	private int id;

	@OneToOne(cascade = CascadeType.ALL) // тот, кто поставил лайк/дизлайк
	@Column(nullable = false, name = "user_id", columnDefinition = "INT")
	private User user;

	@ManyToOne(cascade = CascadeType.ALL)
	@Column(nullable = false, name = "post_id", columnDefinition = "INT")
	private Post post;

	@Column(nullable = false, name = "time", columnDefinition = "DATETIME")
	private Timestamp time;

	@Column(nullable = false, name = "value", columnDefinition = "TINYINT")
	private byte value;

	public PostVotes(int id, User user, Post post, Timestamp time, byte value) {
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

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}
}
