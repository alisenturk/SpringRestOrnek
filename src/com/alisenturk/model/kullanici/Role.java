package com.alisenturk.model.kullanici;

import com.alisenturk.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Role extends BaseEntity {
	
	private static final long serialVersionUID = 6257040782347762540L;
	
	private static final String[] HASH_FIELDS = {"code","id"};
	
	private String	code;
	private String	name;
	private transient long	pageId;
	private String	hashValue;
	
	public Role() {
		super();
		
	}
	public Role(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	public Role(Long id, String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static String[] getHashFields() {
		return HASH_FIELDS;
	}
	@Override
	public String toString() {
		return "Role [code=" + code + ", name=" + name + ", getId()=" + getId()
				+ ", getStatus()=" + getStatus() + ", getCreationDate()="
				+ getCreationDate() + ", getUpdateDate()=" + getUpdateDate()
				+ ", getCreatedBy()=" + getCreatedBy() + ", getUpdatedBy()="
				+ getUpdatedBy() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	public String getHashValue() {
		return hashValue;
	}
	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	public long getPageId() {
		return pageId;
	}
	public void setPageId(long pageId) {
		this.pageId = pageId;
	}
	
}
