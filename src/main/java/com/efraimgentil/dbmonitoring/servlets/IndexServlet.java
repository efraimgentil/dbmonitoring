package com.efraimgentil.dbmonitoring.servlets;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;

@WebServlet(urlPatterns = { "/index"  })
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = -847287836701135896L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("availableDatabases", Arrays.asList( AvailableDatabase.values() ) );
		req.getRequestDispatcher("/WEB-INF/pages/new.jsp").forward( req , resp );
	}

}