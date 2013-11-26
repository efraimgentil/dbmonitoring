var interval = null;
$(document).ready(function(){
	
	$(window).resize(resizeTheResultArea);
	resizeTheResultArea();
	//atualizaMenu();
	
	$("#btnIniciarMonitoramento").click(function(){
		if(!$(this).hasClass("disabled")){
			var host = $("#host").val();
			var usuario = $("#usuario").val();
			var password = $("#password").val();
			var refreshTime = $("#refreshTime").val();
			var titulo = $("#titulo").val();
			refreshTime = refreshTime.replace( /\D/g , "");
			if(refreshTime == ""){
				refreshTime = 10;
			}
			console.log(host + " - " + usuario + " - " + password + " - " + refreshTime + " - " + consulta);
			$.ajax({
				  url: window.location.pathname + "monitor",
				  type : "POST",
				  data: {
					acao     : "iniciar",
					host     : host,
					usuario  : usuario,
					password : password,
				  },
				  success: function(data , status , jqXHR){
					  var json = $.parseJSON(data);
					  if(json.sucesso){
						  $("#resultado").empty();
						  $("#resultado").append("<span style='color:green;'>"+ json.msg + "</span>");
						  $("#resultado").append("<br/><span style='color:green;'>Iniciando consulta...</span>");
						  $("#host").attr("disabled", "disabled");
						  $("#usuario").attr("disabled", "disabled");
						  $("#password").attr("disabled", "disabled");
						  $("#refreshTime").attr("disabled", "disabled");
						  $("#titulo").attr("disabled", "disabled");
						  $("#btnIniciarMonitoramento").addClass("disabled");
						  $("#btnStopMonitoramento").removeClass("disabled");
						  if(titulo != ""){
							 $("#tituloMonitoramento").text( titulo );	
						  }
						  $("#rowBottom").toggle(400);
						  $("#divResultado").css("height" , "85%" );
						  $("#rowBottomStarted").show();
						  atualizar();
						  interval = window.setInterval( atualizar , refreshTime * 1000);
					  }else{
						  $("#resultado").empty();
						  $("#resultado").append("<span style='color:red;'>"+ json.msg + "</span>");
					  }
				  },
				  error: function(jqXHR , status , errorThrown){
					  if(console){
						  console.log("Error: " + status);
						  console.log(jqXHR);
						  console.log(errorThrown);
					  }
				  }
				});
		}
	});
	
	$("#btnStopMonitoramento").click(function(){
		if(!$(this).hasClass("disabled")){
			if(interval != null){
				clearInterval(interval);
				interval = null;
				$("#host").removeAttr("disabled");
				  $("#usuario").removeAttr("disabled");
				  $("#password").removeAttr("disabled");
				  $("#refreshTime").removeAttr("disabled");
				  $("#titulo").removeAttr("disabled", "disabled");
				  $("#btnIniciarMonitoramento").removeClass("disabled");
				  $("#btnStopMonitoramento").addClass("disabled");
				  $("#rowBottomStarted").hide();
				  $("#rowBottom").toggle(400);
				  $("#divResultado").css("height" , "70%" );
			}
		}
	});
});

function atualizar(){
	console.log("Atualizar");
	var consulta = $("#consulta").val();
	var host = $("#host").val();
	$.ajax({
		  url: window.location.pathname + "monitoramento",
		  type : "POST",
		  data: {
			acao     : "atualizar",
			consulta : consulta,
			host     : host
		  },
		  success: function(data , status , jqXHR){
			  console.log("Sucesso" , data);
			  $("#resultado").empty();
			  if(data != ""){
				  var json = $.parseJSON(data);
				  if(json.sucesso){
					  $("#dataAtualizacao").text( json.dataAtualizacao );
					  if(json.result.rows && json.result.rows.length > 0){
						  var j = json.result.rows[0];
						  var arr = []
						  arr.push('<table class="table table-striped"><tr>');
						  for( k in j){
							  arr.push("<th>");
							  arr.push("" + k);
							  arr.push("</th>");
						  }
						  arr.push("</tr>");
						  for(var i = 0 ; i <  json.result.rows.length ; i++){
							  var row = json.result.rows[i];
							  arr.push("<tr>");
							  for( k in row){
								  arr.push("<td>");
								  arr.push(row[k]);
								  arr.push("</td>");
							  }
							  arr.push("</tr>");
						  }
						  arr.push('</table>');
						  $("#resultado").append(arr.join(""));
					  }
				  }else{
					  $("#resultado").append("<span style='color:red;'>"+ json.msg + "</span>");
				  }
			  }
			  
		  },
		  error: function(jqXHR , status , errorThrown){
			  if(console){
				  console.log("Error: " + status);
				  console.log(jqXHR);
				  console.log(errorThrown);
			  }
			  $('#btnStopMonitoramento').trigger('click'); 
		  }
	});
}

function openAddNewMonitorModal(){
	
}

function resizeTheResultArea(){
	var titleHeight = $("#row-title").height();
	var marginBottom = 10;
	var availableHeight = $(document).height();
	$("#row-query").height( availableHeight - ( titleHeight + marginBottom )  );
}
