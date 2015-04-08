package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.Logger;

public class Utils {
	/**
	 * 去空
	 * @param des
	 * @return
	 */
	public static String trimNull(Object des) {
		try {
			if (des == null){
				return "";
			} else{
				return des.toString().trim();
			}
		} catch (NullPointerException npe) {
			return "";
		}
	}
	/**
	 * 将微博解析成JSON格式
	 * @param jtext
	 * @return
	 */
	public static String parseJsonText(String jtext){  
		char leftSymbol='[';  
		char rightSymbol=']';  
		String result="";  
		for(int i=0;i<jtext.length();i++){  
			if(leftSymbol==jtext.charAt(i)){  
				result=jtext.substring(i,jtext.length());  
				break;  
			}  
		}  
		for(int i=result.length();i>0;i--){  
			if(rightSymbol==result.charAt(i-1)){  
				result=result.substring(0, i);  
				break;  
			}  
		}  
		return result;  
	} 
	/**
	 * 将微博信息转换称List
	 * @param rlt
	 * @return
	 */
	public static List<HashMap<String, String>> weiboList(String rlt) {  
		List<HashMap<String, String>> listArrays = new ArrayList<HashMap<String, String>>();
		String result = parseJsonText(rlt);  
		try {  
			JSONArray jsonArrays = new JSONArray(result); 
			for (int i = 0; i < jsonArrays.length(); i++) {  
				JSONObject jsonObject = jsonArrays.getJSONObject(i);  
				HashMap<String, String> map = new HashMap<String, String>();  
				// 获得微博的ID  
				String ID = jsonObject.getString("id");  
				// 获得微博内容  
				String text = jsonObject.getString("text");  
				// 获得微博来源  
				String source = jsonObject.getString("source");  
				// 获得微博的发表时间  
				String created_at = jsonObject.getString("created_at");  
				// 生成user的可用JSON对象  
				JSONObject userJson = jsonObject.getJSONObject("user");  
				// 获得发表微博的用户名  
				String username = userJson.getString("name");
				String userid = userJson.getString("id");
				String description = userJson.getString("description");
				map.put("id", ID);
				map.put("text", text);  
				map.put("name", username); 
				map.put("create_at", created_at);
				map.put("source", "来自" + "【" + source + "】");  
				map.put("description", description);
				map.put("userid", userid);
				if (jsonObject.has("retweeted_status")) {  
					JSONObject retweetedJson = jsonObject  
							.getJSONObject("retweeted_status");  
					// 获得转发微博的内容  
					String retweetedText = retweetedJson.getString("text");  
					map.put("retweetedText", retweetedText);  
					JSONObject retWeetedUserNameJson = retweetedJson  
							.getJSONObject("user");  
					// 获得转发微博内容的用户名  
					String retweetedContent = retWeetedUserNameJson  
							.getString("name");  
					map.put("retweetedContent", retweetedContent + ":"  
							+ retweetedText);  
				}  
				listArrays.add(map);  
			}  
		} catch (JSONException e) {  
			Logger.error("JSON转换异常！");  
			System.out.println(e.getMessage());  
		}  
		return listArrays;  
	} 
}
