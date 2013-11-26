<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:include page="/WEB-INF/template/header.jsp" />
<fmt:setLocale value='en'/>
<fmt:setBundle basename='messages'/>

<h1 id="tituloMonitoramento" style="margin: 0px; padding-left: 20px; padding-top: 5px;" >
	<fmt:message key="monitoring.noTitle" />
</h1>

<div id="divResultado" style="height: 70%; padding: 0px 20px 10px 20px">
	<div id="resultado" style="border: 1px black solid; height: 100%;overflow: auto">
	</div>
</div>

<!-- Quando Start o instervalo mostra essa div -->
<div id="rowBottomStarted" class="row-fluid"  style="display:none">
	<div class="span6">
		<div style="padding-left:20px">Última Atualização: <span id="dataAtualizacao">##/##/#### as ##:##:##</span></div> 
	</div>
	<div class="span6">
		<button id="btnStopMonitoramento" type="button" class="btn btn-danger" style="float:right; margin-right:20px;">Parar</button>
	</div>
</div> 

<div id="rowBottom" class="row-fluid">
	<div class="span6">
		<form class="pure-form pure-form-stacked" style="margin-left: 20px; margin-bottom: 0px">
			<fieldset>
				<label for="consulta">Consulta</label>
				<textarea id="consulta" class="span12" style="resize: none; min-height: 95px"></textarea>
			</fieldset>
		</form>
	</div>
	<div class="span6">
		<form class="pure-form pure-form-stacked" style="margin-bottom: 0px">
			<fieldset>
				<div class="row">
					<div class="span4">
						<label for="host">Host</label> <input id="host" type="text"
							placeholder="127.0.0.1:3306/base "> <label
							for="refreshTime">Tempo Refresh (Segundos)</label> <input
							id="refreshTime" type="text" placeholder="10">
					</div>

					<div class="span4">
						<label for="usuario">Usuário</label>
						<input id="usuario" type="text" placeholder="Usuário">
						<label for="usuario">Titulo</label>
						<input id="titulo"  type="text" placeholder="Titulo">
					</div>

					<div class="span4">
						<label for="password">Password</label>
						<input id="password" class="span11" type="password" placeholder="Password">
						<button id="btnIniciarMonitoramento" type="button" class="btn btn-success" style="margin-top:24px">Iniciar</button>
<!-- 						<button id="btnStopMonitoramento" type="button" class="btn btn-danger disabled" style="margin-top:24px">Parar</button> -->
					</div>
				</div>
			</fieldset>
		</form>
	</div>
</div>

<jsp:include page="/WEB-INF/template/footer.jsp" />