package main.model;

import javax.persistence.*;

@Entity
public class Tag2Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	@ManyToOne(cascade = CascadeType.ALL)
	@Column(nullable = false)
	private Post post;

	@OneToOne(cascade = CascadeType.ALL)
	@Column(nullable = false)
	private Tags tag;

	public Tag2Post (int id, Post post, Tags tag) {
		this.id = id;
		this.post = post;
		this.tag = tag;
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
