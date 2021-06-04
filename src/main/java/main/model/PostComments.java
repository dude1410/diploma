package main.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "post_comments")
public class PostComments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "id", columnDefinition = "INT")
	private Integer id;

	@Column(name = "parent_id", columnDefinition = "INT")
	private Integer parentId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "post_id" ,columnDefinition = "INT")
	private Post post;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "user_id", columnDefinition = "INT")
	private User user;

	@Column(nullable = false, name = "time", columnDefinition = "DATETIME")
	private Timestamp time;

	@Column(nullable = false, name = "text", columnDefinition = "TEXT")
	private String text;

	public PostComments(Integer id,
						Integer parentId,
						Post post,
						User user,
						Timestamp time,
						String text) {
		this.id = id;
		this.parentId = parentId;
		this.post = post;
		this.user = user;
		this.time = time;
		this.text = text;
	}

	public PostComments() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
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
