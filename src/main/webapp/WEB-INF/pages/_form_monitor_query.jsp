<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setLocale value='en' />
<fmt:setBundle basename='messages' />

<form id="form-monitor-query" action="${pageContext.request.contextPath}/monitor"  class="form-horizontal" role="form">
	<input type="hidden" id="action" name="action" value="update" >
	<input type="hidden" id="token" name="token" value="" >
	
	<div class="row" style="margin-top:15px">
		<label for="monitor-title" class="col-sm-2 control-label"> <fmt:message
				key="label.title" />
		</label>
		<div class="col-sm-6">
			<input type="text" class="form-control input-sm" id="monitor-title"
			  name="monitorTitle"
				placeholder='<fmt:message key="label.monitorTitle" />'>
		</div>
		<label for="refresh-time" class="col-sm-1 control-label"> <fmt:message
				key="label.time" />
		</label>
		<div class="col-sm-3">
			<input type="text" class="form-control input-sm" id="refresh-time"
			    name="refreshTime"
				placeholder='<fmt:message key="label.refreshTime" />'>
		</div>
	</div>
	
	<div class="row" style="margin-top:15px">
		<label for="query" class="col-sm-2 control-label"><fmt:message
				key="label.query" />
		</label>
		<div class="col-sm-10">
			<textarea class="form-control input-sm" id="query" rows="5"
			  name="query" placeholder='<fmt:message key="label.query" />' ></textarea>
		</div>
	</div>
</form>

