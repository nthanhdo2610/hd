package com.tinhvan.hd.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name="log")
@SequenceGenerator(name="LOG_SEQUENCE", sequenceName="log_seq", allocationSize=1)
public class LogEntity implements Serializable {

	private static final long serialVersionUID = -8108213279230972124L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOG_SEQUENCE")
	@Column(name="id", updatable=false, nullable=false)
	@Type(type="org.hibernate.type.LongType")
	private Long id;

	@Column(name="label")
	@Type(type="org.hibernate.type.StringType")
	private String label;

	@Column(name="level")
	@Type(type = "org.hibernate.type.ByteType")
	private byte level;

	@Column(name="content")
	@Type(type="org.hibernate.type.TextType")
	private String content;

	@Column(name="created_at")
	@Type(type = "org.hibernate.type.LongType")
	private long createdAt;

	@Basic
	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;


	public LogEntity() {
		Date now = new Date();
		createdAt = now.getTime() / 1000L;
	}

	public void setContent(Object[] content) {
		if (content.length > 0) {
			StringJoiner rs = new StringJoiner(" ");
			for (Object c : content) {
				if (c != null) {
					rs.add(c.toString());
				}
			}
			this.content = rs.toString();
		}
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public byte getLevel() {
		return this.level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
