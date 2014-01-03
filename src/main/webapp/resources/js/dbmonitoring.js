var interval = null;

var Monitor = function(){
	
	var scope = this;
	
	var resultArea = new ResultArea($("#result")[0]);
	
	this.createConnection = function( eve ){
		var btn = eve.target;
		if (this.validateNewMonitorForm() && !btn.classList.contains( "disabled" ) ) {
			btn.classList.add( "disabled" );
			$.ajax({
				url  : $("#form-new-monitor").attr("action") ,
				type : "POST",
				dataType: 'json',
				data : { 
					form   : JSON.stringify( form2js('form-new-monitor', '.', false ) )
				},
				success : function(data , status, jqXHR){
					scope.successfullyOpenConnection(data);
				},
				error : this.manageException,
				complete : function() {
					btn.classList.remove( "disabled" );
				}
			});
		}
	};
	
	this.validateNewMonitorForm = function () {
		var divErrors = $("#form-new-monitor-messages").addClass("hidden").empty()[0];
		var errors = [];
		var host = $("#host").val().replace(/\s/g , "");
		if(host.length == 0)
			errors.push("<b>Host</b> can't be empty");
		var user = $("#user").val();
		if(user.length == 0)
			errors.push("<b>User</b> can't be empty");
		if(errors.length > 0){
			this.addErrorMessage( divErrors, errors.join("<br/>") );
			return false;
		}
		return true;
	};
	
	
	this.successfullyOpenConnection = function( data , status, jqXHR) {
		var json =  data;
		if (json.success) {
			$('#modal-monitor').modal('hide');	
			$('#modal-query').modal('show');
			this.addSuccessMessage( $("#form-monitor-query-messages")[0] , json.message );
			if(json.data)
				$("#form-monitor-query").find("#token").val( json.data.token );
		} else {
			this.addErrorMessage( $("#form-new-monitor-messages")[0] , json.message );
		}
	};

	this.successfullyCreateQuery = function( data , status , jqXHR ){
		if (data.success) {
			resultArea.updateResultArea(data);
			var refreshTime = $("#form-monitor-query").find("#refresh-time").val() || 5;
			var monitorTitle = $("#form-monitor-query").find("#monitor-title").val() || "No title defined";
			$("#title-monitoring").html(monitorTitle);
			interval = window.setInterval( this.update , refreshTime * 1000);
			$('#modal-query').modal('hide');
			$("#btn-stop-monitor").removeClass("hidden");
			$("#btn-add-new-monitor").addClass("hidden");
		}else{
			this.addErrorMessage( $("#form-monitor-query-messages")[0] , data.message );
		}
	};

	this.initiateMonitor = function( eve ) {
		var btn = eve.target;
		if (this.validateMonitorQueryForm() && !btn.classList.contains( "disabled" ) ) {
			btn.classList.add( "disabled" );
			$.ajax({
				url  : $("#form-monitor-query").attr("action") ,
				type : "POST",
				data : {
					form :   JSON.stringify( form2js('form-monitor-query', '.', false ) )
				},
				success : function(data , status, jqXHR){
					scope.successfullyCreateQuery(data);
				},
				error : this.manageException,
				complete : function() {
					btn.classList.remove( "disabled" );
					$("#menu").find("#btn-stop-monitor").removeClass("disabled");
				}
			});
		}
	};
	
	this.validateMonitorQueryForm = function() {
		var divErrors = $("#form-monitor-query-messages").addClass("hidden").empty()[0];
		var errors = [];
		var query = $("#form-monitor-query").find("#query").val();
		if(query.length == 0){
			errors.push("<b>Query</b> can't be empty");
		}
		if(errors.length > 0){
			this.addErrorMessage( divErrors, errors.join("<br/>") );
			return false;
		}
		return true;
	};
	
	this.stopMonitor = function( eve ){
		var btn = eve.target;
		var btnClassList = btn.classList;
		if (!btnClassList.contains("disabled")) {
			btnClassList.add("disabled");
			this.stopInterval();
			$('#modal-query').modal('show');
			$("#btn-add-new-monitor").removeClass("hidden");
			$("#btn-stop-monitor").addClass("hidden");
		}
	};
	
	this.update = function(){
		var token = $("#token").val(); 
		$.ajax({
			url  : $("#form-monitor-query").attr("action"),
			type : "POST",
			data : {
				form : JSON.stringify( 
					{ 
					 action : "update",
					 token : token
					}
				)
			},
			success :  function(data , status, jqXHR){ 
				if(data.success){
					resultArea.updateResultArea( data );
				}else{
					scope.stopInterval();
					resultArea.addErrorMessage( data.message );
				}
			},
			error :  function(jqXHR, status, errorThrown) {
//				scope.manageException ( jqXHR , status , errorThrown );
				resultArea.addErrorMessage( "Well that was unexpected " + errorThrown );
				scope.stopInterval();
			}
		});	
	};
	
	this.stopInterval = function(){
		if (interval != null) {
			window.clearInterval(interval);
			interval = null;
		}
	};
	
	this.addSuccessMessage = function( messageContainer , message ){
		this.clearAlertClass(messageContainer);
		var classList = messageContainer.classList;
		messageContainer.innerHTML = message;
		classList.add("alert-success");
		classList.remove("hidden");
	};

	this.addErrorMessage = function( messageContainer , message ){
		this.clearAlertClass(messageContainer);
		var classList = messageContainer.classList;
		messageContainer.innerHTML = message;
		classList.add("alert-danger");
		classList.remove("hidden");
	};
	
	/**
	 * Gets the list of the class of the @container and removes the alert related classes from it, if having any
	 * @param container
	 */
	this.clearAlertClass = function ( container ){
		var classList = container.classList;
		classList.remove("alert-danger");
		classList.remove("alert-info");
		classList.remove("alert-warning");
		classList.remove("alert-success");
	};
	
	this.manageException = function (jqXHR, status, errorThrown) {
		if (console) {
			console.log("Well that was unexpected" , errorThrown);
			console.log("Status: ", status );
			console.log("Error: " , jqXHR );
		}
	};
	
};

var ResultArea = function(resultArea){
	
	this.resultArea = resultArea;
	
	this.addErrorMessage = function(message){
		$(this.resultArea).empty();
		$(this.resultArea).append("<span style='color:red;'>" + message + "</span>");
	};
	
	this.updateResultArea = function(json){
		$(this.resultArea).empty();
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
			$(this.resultArea).append( arr.join("") );
		}else{
			this.addErrorMessage("Empty result");
		}
	};
	
	this.resizeTheResultArea = function() {
		var titleHeight = $("#row-title").height();
		var marginBottom = 10;
		var availableHeight = $(document).height();
		$("#row-query").height(availableHeight - (titleHeight + marginBottom));
	};
	
};

$(document).ready(function() {
	
	/**
	 * Verify the sizeof the screen and update the result area to match it
	 */
	function resizeTheResultArea() {
		var titleHeight = $("#row-title").height();
		var marginBottom = 10;
		var availableHeight = $(document).height();
		$("#row-query").height(availableHeight - (titleHeight + marginBottom));
	}
	
	$(window).resize( resizeTheResultArea );
	resizeTheResultArea();
	
	var monitor = new Monitor();
	
	$("#btnOpenConnection").click(function(e){
				monitor.createConnection(e);
	});
	
	$("#btnInitiateMonitor").click(function(e){
		monitor.initiateMonitor(e);
	});
	
	$("#btn-stop-monitor").click(function(e) {
		monitor.stopMonitor(e);
	});
	
});