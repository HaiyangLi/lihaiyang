package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import models.Users;
import models.Weibo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import play.Logger;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.XPath;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;
import play.mvc.BodyParser;
import play.mvc.BodyParser.Xml;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import util.MyException;
import util.Utils;
import views.html.view;
import views.html.index;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Application extends Controller{
	
//	@Cached(key="homePage")
	public static Result index(int page){
		String uri = Utils.trimNull(request().uri());
		String code = "";
		String cache_code = Utils.trimNull(Cache.get("code"));
		if(uri != null && uri.indexOf("code") !=-1){
			code = uri.substring(uri.indexOf("="), uri.length());
			Cache.set("code", code);
			return redirect("getWeiboList/"+code);
		}else if(!"".equals(cache_code)){
			List<Weibo> list = Weibo.getAllWeibo(page);
			int pagesize = Weibo.getPageSize();
			return ok(view.render(list,page,pagesize));
		}else{
			return ok(index.render());
		}
	}
	
}