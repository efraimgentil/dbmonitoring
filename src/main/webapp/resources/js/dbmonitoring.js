var interval = null;

var urlMonitor = window.location.pathname + "monitor";
$(document).ready(function() {
	$(window).resize(resizeTheResultArea);
	resizeTheResultArea();

	$("#btnIniciarMonitoramento").click(function() {
		if (!$(this).hasClass("disabled")) {
			$.ajax({
				url : window.location.pathname
					+ "monitor",
				type : "POST",
				data : {
					acao : "iniciar",
					host : host,
					usuario : usuario,
					password : password,
				},
				success : function(	data, status, jqXHR) {
					var json = $.parseJSON(data);
					if (json.sucesso) {
						$("#resultado").empty()
							.append("<span style='color:green;'>"+ json.msg + "</span>")
							.append("<br/><span style='color:green;'>Iniciando consulta...</span>");
						$("#host").attr("disabled","disabled");
						$("#usuario").attr("disabled","disabled");
						$("#password").attr("disabled","disabled");
						$("#refreshTime").attr("disabled","disabled");
						$("#titulo").attr("disabled","disabled");
						$("#btnIniciarMonitoramento").addClass("disabled");
						$("#btnStopMonitoramento").removeClass("disabled");
						if (titulo != "") {
							$("#tituloMonitoramento").text(titulo);
						}
						atualizar();
						interval = window.setInterval(atualizar, refreshTime * 1000);
					} else {
						$("#resultado").empty()
							.append("<span style='color:red;'>"+ json.msg + "</span>");
					}
				},
				error : function(jqXHR, status, errorThrown) {
					if (console) {
						console.log("Error: " + status);
						console.log(jqXHR);
						console.log(errorThrown);
					}
				}
			});
		}
	});

	$("#btnStopMonitoramento").click(function() {
		if (!$(this).hasClass("disabled")) {
			if (interval != null) {
				clearInterval(interval);
				interval = null;
				$("#host").removeAttr("disabled");
				$("#usuario").removeAttr("disabled");
				$("#password").removeAttr("disabled");
				$("#refreshTime").removeAttr("disabled");
				$("#titulo").removeAttr("disabled", "disabled");
				$("#btnIniciarMonitoramento").removeClass("disabled");
				$("#btnStopMonitoramento").addClass("disabled");
			}
		}
	});
});

function atualizar() {
	console.log("Atualizar");
	var consulta = $("#consulta").val();
	var host = $("#host").val();
	$
			.ajax({
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
								arr
										.push('<table class="table table-striped"><tr>');
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
							$("#resultado").append(
									"<span style='color:red;'>" + json.msg
											+ "</span>");
						}
					}

				},
				error : function(jqXHR, status, errorThrown) {
					if (console) {
						console.log("Error: " + status);
						console.log(jqXHR);
						console.log(errorThrown);
					}
					$('#btnStopMonitoramento').trigger('click');
				}
			});
}

function validateNewMonitorForm() {
	var valid = ture;
	var host = $("#host").val();
	var usuario = $("#usuario").val();
	var password = $("#password").val();
	var refreshTime = $("#refreshTime")
			.val();
	var titulo = $("#titulo").val();
	refreshTime = refreshTime.replace(
			/\D/g, "");
	if (refreshTime == "") {
		refreshTime = 10;
	}
	
	return valid;
}

function openAddNewMonitorModal() {

}

function resizeTheResultArea() {
	var titleHeight = $("#row-title").height();
	var marginBottom = 10;
	var availableHeight = $(document).height();
	$("#row-query").height(availableHeight - (titleHeight + marginBottom));
}
