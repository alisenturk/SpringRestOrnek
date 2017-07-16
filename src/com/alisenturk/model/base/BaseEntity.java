package com.alisenturk.model.base;

import java.util.Date;

public class BaseEntity  implements BaseObject{

	private static final long serialVersionUID = -2267474469482968581L;
	private Long    id;   
    private Status  status;
    private Date    creationDate;
    private Date    updateDate;
    private Long    createdBy;
    private Long    updatedBy;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Override
	public String getObjectKey() {		
		return "";
	}
	
    
    
	
}
