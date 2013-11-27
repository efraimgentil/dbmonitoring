var interval = null;

$(document).ready(function() {
	$(window).resize(resizeTheResultArea);
	resizeTheResultArea();

	$("#btnInitiateMonitor").click(function() {
		var btn = this;
		if (validateNewMonitorForm() && !btn.classList.contains( "disabled" ) ) {
			btn.classList.add( "disabled" );
			$.ajax({
				url  : $("#form-new-monitor").attr("action") ,
				type : "POST",
				data : $("#form-new-monitor").serialize() ,
				success : function(	data, status, jqXHR) {
					btn.classList.remove( "disabled" );
					var json =  data; // $.parseJSON(data);
					if (json.sucesso) {
						$("#resultado").empty()
							.append("<span style='color:green;'>"+ json.msg + "</span>")
							.append("<br/><span style='color:green;'>Iniciando consulta...</span>");
						$("#btnIniciarMonitoramento").addClass("disabled");
						$("#btnStopMonitoramento").removeClass("disabled");
						if (titulo != "") {
							$("#tituloMonitoramento").text(titulo);
						}
						atualizar();
						interval = window.setInterval(atualizar, refreshTime * 1000);
					} else {
						$("#form-new-monitor-errors")
						   .html( json.msg  ).removeClass("hidden");
					}
				},
				error : manageException
			});
		}
	});

	$("#btnStopMonitoramento").click(function() {
		if (!$(this).hasClass("disabled")) {
			if (interval != null) {
				clearInterval(interval);
				interval = null;
				$("#btnIniciarMonitoramento").removeClass("disabled");
				$("#btnStopMonitoramento").addClass("disabled");
			}
		}
	});
});

function atualizar() {
	console.log("Atualizar");
	var query = $("#consulta").val();
	var host = $("#host").val();
	$.ajax({
		url : urlMonitor,
		type : "POST",
		data : {
			acao : "update",
			consulta : query,
			host : host
		},
		success : function(data, status, jqXHR) {
			console.log("Sucesso", data);
			$("#resultado").empty();
			if (data != "") {
				var json = $.parseJSON(data);
				if (json.sucesso) {
					$("#dataAtualizacao").text(json.dataAtualizacao);
					if (json.result.rows && json.result.rows.length > 0) {
						var j = json.result.rows[0];
						var arr = []
						arr.push('<table class="table table-striped"><tr>');
						for (k in j) {
							arr.push("<th>");
							arr.push("" + k);
							arr.push("</th>");
						}
						arr.push("</tr>");
						for (var i = 0; i < json.result.rows.length; i++) {
							var row = json.result.rows[i];
							arr.push("<tr>");
							for (k in row) {
								arr.push("<td>");
								arr.push(row[k]);
								arr.push("</td>");
							}
							arr.push("</tr>");
						}
						arr.push('</table>');
						$("#resultado").append(arr.join(""));
					}
				} else {
					$("#resultado").append("<span style='color:red;'>" + json.msg + "</span>");
				}
			}
		},
		error : manageException
	});
}

function validateNewMonitorForm() {
	var divErrors = $("#form-new-monitor-errors").empty();
	var errors = [];
	var host = $("#host").val().replace(/\s/g , "");
	if(host.length == 0)
		errors.push("<b>Host</b> can't be empty");
	var user = $("#user").val();
	if(user.length == 0)
		errors.push("<b>User</b> can't be empty");
	var password = $("#password").val();
	if(password.length == 0)
		errors.push("<b>Password</b> can't be empty");
	
	if(errors.length > 0){
		$(divErrors).html( errors.join("<br/>") ).removeClass("hidden");
		return false;
	}
	return true;
}

/**
 * Disable all inputs in form-new-monitor
 */
function disableFormNewMonitor (){
	$("#form-new-monitor").find("input, select").each(function( index , elem){
		$(elem).attr("disabled","disabled");
	});
};

function enableFormNewMonitor(){
	$("#form-new-monitor").find("input, select").each(function( index , elem){
		$(elem).removeAttr("disabled");
	});
}

function manageException (jqXHR, status, errorThrown) {
	if (console) {
		console.log("Well that was unexpected" , errorThrown); 
		console.log("Error: " + console.log(jqXHR));
	}
}

function openAddNewMonitorModal() {

}

function resizeTheResultArea() {
	var titleHeight = $("#row-title").height();
	var marginBottom = 10;
	var availableHeight = $(document).height();
	$("#row-query").height(availableHeight - (titleHeight + marginBottom));
}
