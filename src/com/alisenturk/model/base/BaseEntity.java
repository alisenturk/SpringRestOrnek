package com.alisenturk.model.base;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class BaseEntity  implements BaseObject{

	private static final long serialVersionUID = -2267474469482968581L;
	private Long    id;   
	
	@JsonInclude(Include.NON_NULL)
    private Status  status;
	
	@JsonInclude(Include.NON_NULL)
    private Date    creationDate;
	
	@JsonInclude(Include.NON_NULL)
    private Date    updateDate;
	
	@JsonInclude(Include.NON_NULL)
    private Long    createdBy;
	
	@JsonInclude(Include.NON_NULL)
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
	public String getObjectDetail() {
		return "";
	}
	@Override
	public String getObjectKey() {		
		return "";
	}
	@Override
	public boolean isViewEveryone() {
		return true;
	}
    
    
    
	
}
