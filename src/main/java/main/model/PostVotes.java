package main.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "post_votes")
public class PostVotes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "id", columnDefinition = "INT")
	private int id;

	@OneToOne(cascade = CascadeType.ALL) // тот, кто поставил лайк/дизлайк
	@JoinColumn(nullable = false, name = "user_id", columnDefinition = "INT")
	private User user;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "post_id", columnDefinition = "INT")
	private Post post;

	@Column(nullable = false, name = "time", columnDefinition = "DATETIME")
	private Timestamp time;

	@Column(nullable = false, name = "value", columnDefinition = "INT")
	private int value;

	public PostVotes(int id, User user, Post post, Timestamp time, int value) {
		this.id = id;
		this.user = user;
		this.post = post;
		this.time = time;
		this.value = value;
	}

	public PostVotes() {
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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
