package main.model;

import javax.persistence.*;

@Entity
@Table(name = "tag2post")
public class Tag2Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "id", columnDefinition = "INT")
	private int id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "post_id", columnDefinition = "INT")
	private Post post;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, name = "tag_id", columnDefinition = "INT")
	private Tags tag;

	public Tag2Post (int id, Post post, Tags tag) {
		this.id = id;
		this.post = post;
		this.tag = tag;
	}

	public Tag2Post() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Tags getTag() {
		return tag;
	}

	public void setTag(Tags tag) {
		this.tag = tag;
	}
}
