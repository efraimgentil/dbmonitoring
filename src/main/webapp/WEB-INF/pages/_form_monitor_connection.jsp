<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setLocale value='en' />
<fmt:setBundle basename='messages' />

<form id="form-new-monitor" action="${pageContext.request.contextPath}/monitor"  class="form-horizontal" role="form">
	<input type="hidden" id="action" name="action" value="open_connection" >
	<div class="form-group">
		<label for="database" class="col-sm-2 control-label"> <fmt:message
				key="label.database" />
		</label>
		<div class="col-sm-10">
			<select id="database" name="database" class="form-control input-sm">
				<c:forEach var="item" items="${availableDatabases}" >
				 	<option value="${item.id}" ><c:out value="${item.description}" /></option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="form-group">
		<label for="host" class="col-sm-2 control-label"> <fmt:message
				key="label.host" />
		</label>
		<div class="col-sm-10">
			<input type="text" class="form-control input-sm" 
			 	id="host" name="host"
				placeholder='<fmt:message key="label.host" />'>
		</div>
	</div>
	<div class="row">
		<label for="user" class="col-xs-2 control-label"> <fmt:message
				key="label.user" />
		</label>
		<div class="col-xs-4">
			<input type="text" class="form-control input-sm" id="user"
			 name="user"
				placeholder='<fmt:message key="label.user" />'>
		</div>
		<label for="password" class="col-xs-2 control-label"> <fmt:message
				key="label.password" />
		</label>
		<div class="col-xs-4">
			<input type="password" class="form-control input-sm" id="password"
			    name="password"
				placeholder='<fmt:message key="label.password" />'>
		</div>
	</div>
</form>

