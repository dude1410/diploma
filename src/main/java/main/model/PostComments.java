package main.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PostComments {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	private int parentId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false)
	private Post post;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false)
	private User user;

	@Column(nullable = false)
	private Date time;

	@Column(nullable = false, columnDefinition = "text")
	private String text;

	public PostComments(int id, int parentId, Post post, User user, Date time, String text) {
		this.id = id;
		this.parentId = parentId;
		this.post = post;
		this.user = user;
		this.time = time;
		this.text = text;
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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
