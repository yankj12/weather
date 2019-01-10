
function saveFood(){
	console.log("saveFood");
	
	var foodName = $("input[name='foodName']").val();
	var price = $("input[name='price']").val();
	var imagePath = $("input[name='imagePath']").val();
	
	$.ajax({
		url : '/food',
		data : 'foodName=' + foodName + '&price=' + price+ '&imagePath=' + imagePath,
		type : 'POST',
		success : function(jd) {
			alert("添加成功");
		}
	});
}