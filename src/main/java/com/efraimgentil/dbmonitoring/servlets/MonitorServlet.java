package com.efraimgentil.dbmonitoring.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.efraimgentil.dbmonitoring.servlets.actions.Action;
import com.efraimgentil.dbmonitoring.servlets.actions.CreateConnection;
import com.efraimgentil.dbmonitoring.servlets.actions.InitiateMonitor;
import com.efraimgentil.dbmonitoring.servlets.actions.InvalidAction;
import com.efraimgentil.dbmonitoring.servlets.actions.UpdateMonitor;

@WebServlet({ "/monitor" })
public class MonitorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String INITIATE = "INITIATE";
	public static final String UPDATE = "UPDATE";
	public static final String OPEN_CONNECTION = "OPEN_CONNECTION";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String actionIdentifier = request.getParameter("action");
		actionIdentifier = actionIdentifier != null ? actionIdentifier.toUpperCase() : "";

		identifyAction( actionIdentifier ).execute(request, response);;
	}
	
	protected Action identifyAction(String actionIdentifier){
		Action action = new InvalidAction();
		switch (actionIdentifier) {
			case OPEN_CONNECTION:
				action = new CreateConnection();
				break;
			case INITIATE:
				action = new InitiateMonitor();
				break;
			case UPDATE:
				action = new UpdateMonitor();
				break;
		}
		return action;
	}
	
}