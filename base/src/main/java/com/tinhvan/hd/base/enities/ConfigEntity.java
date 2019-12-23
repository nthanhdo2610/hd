package com.tinhvan.hd.base.enities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tinhvan.hd.base.util.VarCharStringArrayType;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@Entity
@TypeDefs({
	@TypeDef(
			typeClass = VarCharStringArrayType.class,
			defaultForType = String[].class
	)
})
@Table(name = "system_config")
public class ConfigEntity {

	@Id
	@Column(name="key", columnDefinition="VARCHAR(100)")
	private String key;

	@Column(name="value", columnDefinition="VARCHAR(255)[]")
	private String[] value;

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String[] getValue() {
		return this.value;
	}

	public void setValue(String[] value) {
		this.value = value;
	}

	public ConfigEntity() {}


}
