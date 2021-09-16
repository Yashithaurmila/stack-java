package com.staging.stack.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Instance {
	
	@Id
	private long id;
	private String name;
	private long teamId;
	private String status;
	
	public Instance(long id, String name, long teamId, String status) {
		super();
		this.id = id;
		this.name = name;
		this.teamId = teamId;
		this.status = status;
	}
	public Instance() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTeamId() {
		return teamId;
	}
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	@Override
	public String toString() {
		return "Instance [id=" + id + ", name=" + name + ", teamId=" + teamId + ", status=" + status + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, name, status, teamId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instance other = (Instance) obj;
		return id == other.id && Objects.equals(name, other.name) && Objects.equals(status, other.status)
				&& teamId == other.teamId;
	}

}
