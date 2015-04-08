package controllers;

import java.util.HashMap;
import java.util.List;

import models.Users;
import models.Weibo;

import org.json.JSONArray;
import org.json.JSONObject;
import views.html.view;
import play.Logger;
import play.cache.Cache;
import play.data.Form;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import util.MyException;
import util.Utils;

import com.fasterxml.jackson.databind.JsonNode;

public class WeiboController extends Controller{
	/**
	 * 获取微博列表
	 * @param code
	 * @return
	 */
	public static Result getWeiboList(String code) throws Exception{
		if(code == null || "".equals(code)){
			code = Utils.trimNull(Cache.get("code"));
		}
		String url = "https://api.weibo.com/oauth2/access_token?client_id=3856530859&client_secret=ce019c7a93abdb9a1f11d1fd3104945f&grant_type=authorization_code&redirect_uri=127.0.0.1:9000&code"+code;
		Promise<Result> jsonPromise = WS.url(url).post(url).map(
	        new Function<WSResponse, Result>() {
	            public Result apply(WSResponse response) {
	            	JsonNode json = response.asJson();
	            	String access_token = json.findPath("access_token").textValue();
	            	session("access_token", access_token);
	            	String uid = json.findPath("uid").textValue();
	            	
	            	//使用access_token和uid获取微博列表，并处理插入到数据库中
	            	try {
	            		userAdd(access_token,uid);
	            		getJson(access_token,uid);
					} catch (Exception e) {
						Logger.error("获取微博json数据失败......");
//						throw new MyException("获取微博json失败......");
					}
	                return ok(json);
	            }
	        }
		);
		
		List<Weibo> list = Weibo.getAllWeibo(0);
		int pagesize = Weibo.getPageSize();
		return ok(view.render(list,0,pagesize));
	}
	/**
	 * 获取微博的结果
	 * @param access_token
	 * @param uid
	 */
	public static void getJson(String access_token,String uid) throws MyException{
		String url = "https://api.weibo.com/2/statuses/friends_timeline.json?access_token="+access_token;
		Promise<JsonNode> jsonPromise = WS.url(url).get().map(
	        new Function<WSResponse, JsonNode>() {
	            public JsonNode apply(WSResponse response){
	            	JsonNode json = response.asJson();
	            	try {
	            		addWeibo(json);
	            		
					} catch (Exception e) {
						Logger.error("数据库操作异常！");
					}
	                return json;
	            }
	        }
		);
	}
	public static void userAdd(String access_token,String uid) throws Exception{
		String url = "https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;
		Promise<JsonNode> jsonPromise = WS.url(url).get().map(
	        new Function<WSResponse, JsonNode>() {
	            public JsonNode apply(WSResponse response) throws Exception{
	            	JsonNode json = response.asJson();
	            	try {
	            		String result = Utils.parseJsonText("["+json.toString()+"]");
	            		JSONArray jsonArrays = new JSONArray(result);
		            	for(int i=0;i<jsonArrays.length();i++){
		            		JSONObject jsonObject = jsonArrays.getJSONObject(i);
		            		Users u = new Users();
		            		u.userid = Long.valueOf(jsonObject.getString("id"));
		            		u.username = jsonObject.getString("name");
		            		u.description = jsonObject.getString("description");
		            		Users.saveUsers(u);
		            	}
					} catch (Exception e) {
						e.printStackTrace();
					}
	            	
	                return json;
	            }
	        }
		);
	}
	/**
	 * 插入微博
	 * @param json
	 */
	public static void addWeibo(JsonNode json) throws Exception{
		int size = json.size();
		String js = json.toString();
		List<HashMap<String, String>> listArrays = Utils.weiboList(js);
		//Ebean.beginTransaction();
		try {
			Weibo.del();
			for(int i=0;i<listArrays.size();i++){
				Weibo fs = new Weibo();
				HashMap map = listArrays.get(i);
				fs.id = Long.valueOf(Utils.trimNull(map.get("id")));
				fs.text = Utils.trimNull(map.get("text"));
				fs.username = Utils.trimNull(map.get("name"));
				fs.source = Utils.trimNull(map.get("source"));
				fs.retweetedContent = Utils.trimNull(map.get("retweetedContent"));
				fs.retweetedText = Utils.trimNull(map.get("retweetedText"));
				Weibo.saveWeibo(fs);
				Logger.debug("add a row, id:"+fs.id);
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage());
			throw new MyException("数据库操作异常！");
		}
//		Ebean.commitTransaction();
//		Ebean.endTransaction();
	}
	/**
	 * 编辑微博
	 * @return
	 */
	public static Result editWeibo(){
		//获取view中提交的表单
		Form<Weibo> userForm = Form.form(Weibo.class);
		Weibo frs = userForm.bindFromRequest().get();
		String text = frs.text;
		Weibo fs = Weibo.find.byId(frs.id);
	    fs.text = text;
	    try {
	    	Weibo.updateWeibo(fs);
	    	Logger.debug("update a row, id:"+frs.id);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
		}
	    
		return redirect(routes.Application.index(0));
	}
	/**
	 * 单一删除和批量删除
	 * @param id
	 * @return
	 */
	public static Result del(String id){
		id = Utils.trimNull(id);
		if(id.indexOf(",")!=-1){
			String[] ids = id.split(",");
			for(int i=0;i<ids.length;i++){
				delMethod(ids[i]);
			}
		}else{
			delMethod(id);
		}
		List<Weibo> list = Weibo.getAllWeibo(0);
		return redirect(routes.Application.index(0));
	}
	/**
	 * 删除方法
	 * @param id
	 */
	public static void delMethod(String id){
		try {
			Long ids = Long.valueOf(id);
			Weibo fs = Weibo.find.byId(ids);
			fs.delete();
			Logger.debug("delete a row, id:"+id);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
		}
	}
}
