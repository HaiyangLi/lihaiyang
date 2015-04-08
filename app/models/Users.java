package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Users extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3813144576818317015L;
	@Id
	public Long userid;
	public String username;
	public String description;
	
	public static Finder<Long, Users> find = new Finder<Long, Users>(Long.class, Users.class); 
	
	public static void saveUsers(Users users){
		Users u = find.byId(users.userid);
		if(u == null){
			users.save();
		}else{
			return;
		}
	}
}
