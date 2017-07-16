package com.alisenturk.model.base;

import java.io.Serializable;
import java.util.Date;

public class ProcessLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long 	id;
	private Date 	processDate;
	private String 	byUser;
	private long 	personelNo;
	private String 	description;
	private String 	className;
	private String 	methodName;
	private String 	processData;
	private String 	token;
	private String 	lattitude;
	private String 	longitude;
	private String 	ipAddress;
	private String 	iconClass;
	private boolean	viewTimeline = false;
	private String	detailInfo;
	private String	group;
	private String	statusCode;
	private String	statusMessage;
	private boolean viewEveryone = true;
	/* Transient */
	private int min;
	private int max;

	public ProcessLog() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public String getByUser() {
		return byUser;
	}

	public void setByUser(String byUser) {
		this.byUser = byUser;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getProcessData() {
		return processData;
	}

	public void setProcessData(String processData) {
		this.processData = processData;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return enlem
	 */
	public String getLattitude() {
		return lattitude;
	}

	/**
	 * @param enlem
	 */
	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}

	/**
	 * @return boylam
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            boylam
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public long getPersonelNo() {
		return personelNo;
	}

	public void setPersonelNo(long personelNo) {
		this.personelNo = personelNo;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return "ProcessLog [id=" + id + ", processDate=" + processDate
				+ ", byUser=" + byUser + ", personelNo=" + personelNo
				+ ", description=" + description + ", className=" + className
				+ ", methodName=" + methodName + ", processData=" + processData
				+ ", token=" + token + ", lattitude=" + lattitude
				+ ", longitude=" + longitude + ", ipAddress=" + ipAddress
				+ ", iconClass=" + iconClass + ", viewTimeline=" + viewTimeline
				+ ", detailInfo=" + detailInfo + ", min=" + min + ", max="
				+ max + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ProcessLog)) {
			return false;
		}
		ProcessLog other = (ProcessLog) obj;
		if (byUser == null) {
			if (other.byUser != null) {
				return false;
			}
		} else if (!byUser.equals(other.byUser)) {
			return false;
		}
		if (className == null) {
			if (other.className != null) {
				return false;
			}
		} else if (!className.equals(other.className)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (detailInfo == null) {
			if (other.detailInfo != null) {
				return false;
			}
		} else if (!detailInfo.equals(other.detailInfo)) {
			return false;
		}
		if (group == null) {
			if (other.group != null) {
				return false;
			}
		} else if (!group.equals(other.group)) {
			return false;
		}
		if (iconClass == null) {
			if (other.iconClass != null) {
				return false;
			}
		} else if (!iconClass.equals(other.iconClass)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (ipAddress == null) {
			if (other.ipAddress != null) {
				return false;
			}
		} else if (!ipAddress.equals(other.ipAddress)) {
			return false;
		}
		if (lattitude == null) {
			if (other.lattitude != null) {
				return false;
			}
		} else if (!lattitude.equals(other.lattitude)) {
			return false;
		}
		if (longitude == null) {
			if (other.longitude != null) {
				return false;
			}
		} else if (!longitude.equals(other.longitude)) {
			return false;
		}
		if (max != other.max) {
			return false;
		}
		if (methodName == null) {
			if (other.methodName != null) {
				return false;
			}
		} else if (!methodName.equals(other.methodName)) {
			return false;
		}
		if (min != other.min) {
			return false;
		}
		if (personelNo != other.personelNo) {
			return false;
		}
		if (processData == null) {
			if (other.processData != null) {
				return false;
			}
		} else if (!processData.equals(other.processData)) {
			return false;
		}
		if (processDate == null) {
			if (other.processDate != null) {
				return false;
			}
		} else if (!processDate.equals(other.processDate)) {
			return false;
		}
		if (statusCode == null) {
			if (other.statusCode != null) {
				return false;
			}
		} else if (!statusCode.equals(other.statusCode)) {
			return false;
		}
		if (statusMessage == null) {
			if (other.statusMessage != null) {
				return false;
			}
		} else if (!statusMessage.equals(other.statusMessage)) {
			return false;
		}
		if (token == null) {
			if (other.token != null) {
				return false;
			}
		} else if (!token.equals(other.token)) {
			return false;
		}
		if (viewTimeline != other.viewTimeline) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((byUser == null) ? 0 : byUser.hashCode());
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((detailInfo == null) ? 0 : detailInfo.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result
				+ ((iconClass == null) ? 0 : iconClass.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result
				+ ((lattitude == null) ? 0 : lattitude.hashCode());
		result = prime * result
				+ ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result + max;
		result = prime * result
				+ ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + min;
		result = prime * result + (int) (personelNo ^ (personelNo >>> 32));
		result = prime * result
				+ ((processData == null) ? 0 : processData.hashCode());
		result = prime * result
				+ ((processDate == null) ? 0 : processDate.hashCode());
		result = prime * result
				+ ((statusCode == null) ? 0 : statusCode.hashCode());
		result = prime * result
				+ ((statusMessage == null) ? 0 : statusMessage.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + (viewTimeline ? 1231 : 1237);
		return result;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public boolean isViewTimeline() {
		return viewTimeline;
	}

	public void setViewTimeline(boolean viewTimeline) {
		this.viewTimeline = viewTimeline;
	}

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public boolean isViewEveryone() {
		return viewEveryone;
	}

	public void setViewEveryone(boolean viewEveryone) {
		this.viewEveryone = viewEveryone;
	}

}
