package main.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "post_comments")
public class PostComments implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "id", columnDefinition = "INT")
	private int id;

	@Column(name = "parent_id", columnDefinition = "INT")
	private int parentId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "post_id", columnDefinition = "INT")
	private Post post;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "user_id", columnDefinition = "INT")
	private User user;

	@Column(nullable = false, name = "time", columnDefinition = "DATETIME")
	private Timestamp time;

	@Column(nullable = false, name = "text", columnDefinition = "TEXT")
	private String text;

	public PostComments(int id, int parentId, Post post, User user, Timestamp time, String text) {
		this.id = id;
		this.parentId = parentId;
		this.post = post;
		this.user = user;
		this.time = time;
		this.text = text;
	}

	public PostComments() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post postId) {
		this.post = post;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
