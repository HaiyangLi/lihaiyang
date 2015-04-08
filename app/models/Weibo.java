package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

import play.Logger;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Weibo extends Model{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3756339701137903293L;
	
	public String created_at;					//微博创建时间
	@Id
	public Long id;								//微博ID  
	public Long mid;							//微博MID 
	public String idstr;						//字符串型的微博ID
	public String text;							//微博信息内容 
	public String source;						//微博来源 
	public String username;						//该条微博的所有者的名字
	public String retweetedText;				//转发微博的内容 
	public String retweetedContent;				//转发微博内容的用户名
//	public boolean favorited;					//是否已收藏，true：是，false：否  
//	public boolean truncated;					//是否被截断，true：是，false：否  
//	public String in_reply_to_status_id;		//（暂未支持）回复ID  
//	public String in_reply_to_user_id;			//（暂未支持）回复人UID  
//	public String in_reply_to_screen_name;		//（暂未支持）回复人昵称  
//	public String thumbnail_pic;				//缩略图片地址，没有时不返回此字段  
//	public String bmiddle_pic;					//中等尺寸图片地址，没有时不返回此字段  
//	public String original_pic;					//原始图片地址，没有时不返回此字段  
//	public Object geo;							//地理信息字段 详细  
//	public Object user;							//微博作者的用户信息字段 详细  
//	public Object retweeted_status;				//被转发的原微博信息字段，当该微博为转发微博时返回 详细  
//	public int reposts_count;					//转发数  
//	public int comments_count;					//评论数  
//	public int attitudes_count;					//表态数  
//	public int mlevel;							//暂未支持  
//	public Object visible;						//微博的可见性及指定可见分组信息。该object中type取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；list_id为分组的组号  
//	public Object pic_ids;						//微博配图ID。多图时返回多图ID，用来拼接图片url。用返回字段thumbnail_pic的地址配上该返回字段的图片ID，即可得到多个图片url。  
//	public Object[] ad;							//微博流内的推广微博ID 
	
	public static Finder<Long,Weibo> find = new Finder<Long,Weibo>(Long.class, Weibo.class);
	
	/**
	 * 保存
	 * @param weibo
	 */
	public static void saveWeibo(Weibo weibo){
		weibo.save();
	}
	/**
	 * 更新
	 * @param wb
	 */
	public static void updateWeibo(Weibo wb){
		wb.update();
	}
	/**
	 * 查询当前页数据
	 * @param pageNum
	 * @return
	 */
	public static List<Weibo> getAllWeibo(int pageNum){
		int pagesize = getPageSize();
		if(pageNum<0){
			pageNum = 0;
		}
		if(pageNum>pagesize){
			pageNum = pagesize;
		}
		Page<Weibo> pages = find.where().orderBy("id desc").findPagingList(5).getPage(pageNum);
		List<Weibo> w = pages.getList();
		return w;
	}
	/**
	 * 得到总页数
	 * @return
	 */
	public static int getPageSize(){
		int pagesize = find.where().findPagingList(5).getPageSize();
		return pagesize;
	}
	/**
	 * 删除所有记录
	 */
	public static void del(){
		List<Weibo> w = find.all();
		if(w != null && w.size() != 0){
			try {
				for(int i=0;i<w.size();i++){
					Ebean.delete(w.get(i));
					Logger.debug("delete a row, id:"+w.get(i).id);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.debug(e.getMessage());
			}
		}
	}
}
