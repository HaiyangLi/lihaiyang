function edit(id){
	$("#"+id).attr("disabled",false);
}
function save(id){
	var text = $("#"+id).val();
	$("#id").val(id);
	$("#text").val(text);
	$("#saveWeibo").submit();
}
	
	
function checkAll(obj){
	var array = $(".subCheck");
	for(var i in array){
		array[i].checked=obj.checked;
	}
}
function getWeibo(){
	window.location.assign("https://api.weibo.com/oauth2/authorize?client_id=3856530859&response_type=code&redirect_uri=127.0.0.1:9000");
}
