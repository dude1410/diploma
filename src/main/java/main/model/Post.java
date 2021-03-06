package main.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "id", columnDefinition = "INT")
	private Integer id;

	@Column(nullable = false, name = "is_active", columnDefinition = "INT")
	private Integer isActive;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "moderation_status", columnDefinition = "ENUM")
	private ModerationStatus moderationStatus;

	@Column(name = "moderator_id", columnDefinition = "INT")
	private Integer moderatorId;

	@ManyToOne()
	@JoinColumn(nullable = false, name = "user_id", columnDefinition = "INT")
	private User user;

	@Column(nullable = false, name = "time", columnDefinition = "DATETIME")
	private Timestamp time;

	@Column(nullable = false, name = "title", columnDefinition = "VARCHAR(255)")
	private String title;

	@Column(nullable = false, name = "text", columnDefinition = "TEXT")
	private String text;

	@Column(nullable = false, name = "view_count", columnDefinition = "INT")
	private Integer viewCount;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PostComments> postComments = new HashSet<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PostVotes> postVotes = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "tag2post",
			joinColumns = @JoinColumn(name = "post_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private Set<Tags> tags = new HashSet<>();

	public Set<Tags> getTags() {
		return tags;
	}

	public Post(Integer id, Integer isActive, ModerationStatus moderationStatus, Integer moderatorId,
				User user, Timestamp time, String title, String text, Integer viewCount){
		this.id = id;
		this.isActive = isActive;
		this.moderationStatus = moderationStatus;
		this.moderatorId = moderatorId;
		this.user = user;
		this.time = time;
		this.title = title;
		this.text = text;
		this.viewCount = viewCount;
	}

	public Post() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer isIs_active() {
		return isActive;
	}

	public void setIs_active(Integer is_active) {
		this.isActive = is_active;
	}

	public ModerationStatus getModeration_status() {
		return moderationStatus;
	}

	public void setModeration_status(ModerationStatus moderation_status) {
		this.moderationStatus = moderation_status;
	}

	public Integer getModerator_id() {
		return moderatorId;
	}

	public void setModerator_id(Integer moderator_id) {
		this.moderatorId = moderator_id;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Set<PostComments> getPostComments() {
		return postComments;
	}

	public void setPostComments(Set<PostComments> postComments) {
		this.postComments = postComments;
	}

	public Set<PostVotes> getPostVotes() {
		return postVotes;
	}

	public void setPostVotes(Set<PostVotes> postVotes) {
		this.postVotes = postVotes;
	}
}
