package com.tinhvan.hd.base.enities;

import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.util.VarCharStringArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@TypeDefs({
		@TypeDef(
				typeClass = VarCharStringArrayType.class,
				defaultForType = String[].class
		)
})
@Table(name="authorize_user")
@SequenceGenerator(name="AUTHORIZE_USER_SEQUENCE", sequenceName="authorize_user_seq", allocationSize=1)
public class AuthorizeUserEntity implements Serializable, HDPayload {

	private static final long serialVersionUID = -8108213279230972124L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AUTHORIZE_USER_SEQUENCE")
	@Column(name="id", updatable=false, nullable=false)
	@Type(type="org.hibernate.type.LongType")
	private Long id;

	@Column(name="role_id")
	@Type(type="org.hibernate.type.LongType")
	private Long roleId;

	@Column(name="path")
	private String path;

	@Column(name="status")
	@Type(type="org.hibernate.type.IntegerType")
	private Integer status;



	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public void validatePayload() {

	}
}
