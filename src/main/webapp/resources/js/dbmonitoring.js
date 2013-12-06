var interval = null;

$(document).ready(function() {
	$(window).resize(resizeTheResultArea);
	resizeTheResultArea();

	$("#btnOpenConnection").click(function() {
		var btn = this;
		if (validateNewMonitorForm() && !btn.classList.contains( "disabled" ) ) {
			btn.classList.add( "disabled" );
			$.ajax({
				url  : $("#form-new-monitor").attr("action") ,
				type : "POST",
				dataType: 'json',
				data : { 
					action : $("#form-new-monitor").find("#action").val(),
					form   : JSON.stringify( form2js('form-new-monitor', '.', false ) )
				},
				success : successfullyOpenConnection,
				error : manageException,
				complete : function() {
					btn.classList.remove( "disabled" );
				}
			});
		}
	});
	
	$("#btnInitiateMonitor").click(function(){
		var btn = this;
		if (validateMonitorQueryForm() && !btn.classList.contains( "disabled" ) ) {
			btn.classList.add( "disabled" );
			$.ajax({
				url  : $("#form-monitor-query").attr("action") ,
				type : "POST",
				data : $("#form-monitor-query").serialize() ,
				success : successfullyCreateQuery,
				error : manageException,
				complete : function() {
					btn.classList.remove( "disabled" );
				}
			});
		}
	});
	

	$("#btnStopMonitor").click(function() {
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

function update() {
	var query = $("#query").val();
	var token = $("#token").val();
	$.ajax({
		url  : $("#form-monitor-query").attr("action"),
		type : "POST",
		data : {
			action : "update",
			query : query,
			token : token
		},
		success : function(data, status, jqXHR) {
			updateResultArea(data);
		},
		error : manageException
	});
}

function updateResultArea(json){
	$("#result").empty();
	if (json != "") {
		if (json.success && json.data) {
			var rows = json.data.rows;
			$("#dataAtualizacao").text(json.dataAtualizacao);
			if (rows && rows.length > 0) {
				var j = rows[0];
				var arr = [];
				arr.push('<table class="table table-striped"><tr>');
				for (k in j) {
					arr.push("<th>");
					arr.push("" + k);
					arr.push("</th>");
				}
				arr.push("</tr>");
				var rowsLength = rows.length;
				for (var i = 0; i < rowsLength ; i++) {
					var row = rows[i];
					arr.push("<tr>");
					for (k in row) {
						arr.push("<td>");
						arr.push(row[k]);
						arr.push("</td>");
					}
					arr.push("</tr>");
				}
				arr.push('</table>');
				$("#result").append( arr.join("") );
			}
		} else {
			$("#result").append("<span style='color:red;'>" + json.msg + "</span>");
		}
	}
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

function successfullyOpenConnection( data , status, jqXHR) {
	var json =  data;
	if (json.success) {
		$('#modalMonitor').modal('hide');	
		$('#modalQuery').modal('show');
		addSuccessMessage( $("#form-monitor-query-messages")[0] , json.message );
		if(json.data)
			$("#form-monitor-query").find("#token").val( json.data.token );
	} else {
		addErrorMessage( $("#form-new-monitor-messages")[0] , json.message );
	}
}

function successfullyCreateQuery( data , status , jqXHR ){
	if (data.success) {
		updateResultArea(data);
		var refreshTime = $("#form-monitor-query").find("#refresh-time").val() || 5;
		interval = window.setInterval( update , refreshTime * 1000);
		$('#modalQuery').modal('hide');
	}else{
		addErrorMessage( $("#form-monitor-query-messages")[0] , data.msg );
	}
}

function addSuccessMessage( messageContainer , message ){
	clearAlertClass(messageContainer);
	var classList = messageContainer.classList;
	messageContainer.innerHTML = message;
	classList.add("alert-success");
	classList.remove("hidden");
}

function addErrorMessage( messageContainer , message ){
	clearAlertClass(messageContainer);
	var classList = messageContainer.classList;
	messageContainer.innerHTML = message;
	classList.add("alert-danger");
	classList.remove("hidden");
}

/**
 * Gets the list of the class of the @container and removes the alert related classes from it, if having any
 * @param container
 */
function clearAlertClass( container ){
	var classList = container.classList;
	classList.remove("alert-danger");
	classList.remove("alert-info");
	classList.remove("alert-warning");
	classList.remove("alert-success");
}

/**
 * Verify the sizeof the screen and update the result area to match it
 */
function resizeTheResultArea() {
	var titleHeight = $("#row-title").height();
	var marginBottom = 10;
	var availableHeight = $(document).height();
	$("#row-query").height(availableHeight - (titleHeight + marginBottom));
}

function validateNewMonitorForm() {
	var divErrors = $("#form-new-monitor-messages").addClass("hidden").empty()[0];
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
		addErrorMessage( divErrors, errors.join("<br/>") );
		return false;
	}
	return true;
}

function validateMonitorQueryForm() {
	var divErrors = $("#form-monitor-query-messages").addClass("hidden").empty()[0];
	var errors = [];
	var query = $("#form-monitor-query").find("#query").val();
	if(query.length == 0){
		errors.push("<b>Query</b> can't be empty");
	}
	if(errors.length > 0){
		addErrorMessage( divErrors, errors.join("<br/>") );
		return false;
	}
	return true;
}
