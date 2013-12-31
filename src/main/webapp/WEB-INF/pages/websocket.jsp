<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<jsp:include page="/WEB-INF/template/header.jsp" />
<fmt:setLocale value='en' />
<fmt:setBundle basename='messages' />

<div class="container">
	<div id="row-title" class="row">
		<div class="col-md-8">
			<h1 id="title-monitoring">
				<fmt:message key="monitoring.noTitle" />
			</h1>
		</div>
		<div class="col-md-4">
			<div id="menu">
				<button data-toggle="modal" data-target="#modal-monitor"
					class="btn btn-success" id="btn-add-new-monitor">
					<fmt:message key="button.addNewMonitor" />
				</button>
				<button class="btn btn-danger hidden" id="btn-stop-monitor">
					<fmt:message key="button.stopMonitor" />
				</button>
			</div>
		</div>
	</div>
	<div id="row-query" class="row-fluid">
		<div class="result-container">
			<div id="result"></div>
		</div>
	</div>
</div>

<div class="modal fade" id="modal-monitor" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title" id="myModalLabel">
					<fmt:message key="addMonitor.title" />
				</h4>
			</div>
			<div class="modal-body">
				<div id="form-new-monitor-messages" class="alert hidden"></div>
				<jsp:include page="_form_monitor_connection.jsp" />
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="button.cancel" />
				</button>
				<button type="button" id="btnOpenConnection" class="btn btn-primary">
					<fmt:message key="button.createConnection" />
				</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="modal-query" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title" id="myModalLabel">
					<fmt:message key="monitorQuery.title" />
				</h4>
			</div>
			<div class="modal-body">
				<div id="form-monitor-query-messages" class="alert hidden"></div>
				<jsp:include page="_form_monitor_query.jsp" />
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<fmt:message key="button.cancel" />
				</button>
				<button type="button" id="btnInitiateMonitor"
					class="btn btn-primary">
					<fmt:message key="button.initiate" />
				</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var Monitor = {};
	window.onload = function(){
		
		Monitor.connect = (function(host) {
			if ('WebSocket' in window) {
				Monitor.socket = new WebSocket(host);
			} else if ('MozWebSocket' in window) {
				Monitor.socket = new MozWebSocket(host);
			} else {
				console.log('Error: WebSocket is not supported by this browser.');
				return;
			}

			Monitor.socket.onopen = function() {
				console.log('Info: WebSocket connection opened.');
			};

			Monitor.socket.onclose = function() {
				console.log('Info: WebSocket closed.');
			};

			Monitor.socket.onmessage = function(message) {
				console.log(message.data);
			};
		});
		
		Monitor.sendMessage = (function(message) {
			console.log("Enviando dados" , message);
			Monitor.socket.send(message);
		});
		
		Monitor.initialize = function() {
			if (window.location.protocol == 'http:') {
				Monitor.connect('ws://' + window.location.host
						+ '/dbmonitoring/webs/monitor');
			} else {
				Monitor.connect('wss://' + window.location.host
						+ '/dbmonitoring/webs/monitor');
			}
		};
		
		Monitor.initialize();
	};
</script>
<jsp:include page="/WEB-INF/template/footer.jsp" />