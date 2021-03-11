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
	private Post postId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false)
	private User userId;

	@Column(nullable = false)
	private Date time;

	@Column(nullable = false)
	private String text;

	public PostComments(int id, int parentId, Post postId, User userId, Date time, String text) {
		this.id = id;
		this.parentId = parentId;
		this.postId = postId;
		this.userId = userId;
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

	public Post getPostId() {
		return postId;
	}

	public void setPostId(Post postId) {
		this.postId = postId;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
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
