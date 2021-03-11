package main.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	@Column(nullable = false)
	private boolean isActive;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "enum('NEW', 'ACCEPTED', 'DECLINED')")
	private ModerationStatus moderationStatus;

	@Column
	private int moderatorId;

	@ManyToOne()
	@JoinColumn(nullable = false)
	private User userId;

	@Column(nullable = false)
	private Date time;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String text;

	@Column(nullable = false)
	private int viewCount;

	public Post(int id, boolean isActive, ModerationStatus moderationStatus, int moderatorId,
				User userId, Date time, String title, String text, int viewCount){
		this.id = id;
		this.isActive = isActive;
		this.moderationStatus = moderationStatus;
		this.moderatorId = moderatorId;
		this.userId = userId;
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

	public boolean isIs_active() {
		return isActive;
	}

	public void setIs_active(boolean is_active) {
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

	public User getUser_id() {
		return userId;
	}

	public void setUser_id(User user_id) {
		this.userId = user_id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
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
