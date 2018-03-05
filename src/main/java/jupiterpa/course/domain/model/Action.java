package jupiterpa.course.domain.model;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@Document
public class Action {
	
	@Id
	private String id;
	private String user;
	private Date timestamp;
	
	public Action() {}
	public Action(String id) {
		this.id = id;
		this.user = getCurrentUser();
		this.timestamp = new Date(System.currentTimeMillis());
	}
	
	private String getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			try {
				User u = (User) auth.getPrincipal();
				if (u != null) {
					return u.getUsername();
				}	
			} catch (ClassCastException exp) {
				// do nothing
			}
		}
		return "";
	}
	
	public void update() {
		this.user = getCurrentUser();
		this.timestamp = new Date(System.currentTimeMillis());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		return "Action " + id + " ( by " + user + " at " + timestamp.toLocaleString() + " )"; 
	}
	public String getId() {
		return id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public String getUser() {
		return user;
	}
		
}
