package main.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, name = "id", columnDefinition = "INT")
	private int id;

	@Column(nullable = false, name = "is_active", columnDefinition = "TINYINT")
	private byte isActive;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "moderation_status", columnDefinition = "ENUM")
	private ModerationStatus moderationStatus;

	@Column(name = "moderator_id", columnDefinition = "INT")
	private int moderatorId;

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
	private int viewCount;

	public Post(int id, byte isActive, ModerationStatus moderationStatus, int moderatorId,
				User user, Timestamp time, String title, String text, int viewCount){
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte isIs_active() {
		return isActive;
	}

	public void setIs_active(byte is_active) {
		this.isActive = is_active;
	}

	public ModerationStatus getModeration_status() {
		return moderationStatus;
	}

	public void setModeration_status(ModerationStatus moderation_status) {
		this.moderationStatus = moderation_status;
	}

	public int getModerator_id() {
		return moderatorId;
	}

	public void setModerator_id(int moderator_id) {
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

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
}
