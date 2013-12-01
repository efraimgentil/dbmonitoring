package com.efraimgentil.dbmonitoring.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;
import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;

@WebServlet({ "/monitor" })
public class MonitorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String INITIATE = "INITIATE";
	private static final String UPDATE = "UPDATE";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		String action = request.getParameter("action") != null ? request.getParameter("action") : null;

		PrintWriter pw = response.getWriter();
		if ( INITIATE.equalsIgnoreCase(action) ) {
			request.getParameterMap();
			Object databaseO = request.getParameter("database");
			Object hostO = request.getParameter("host");
			Object userO = request.getParameter("user");
			Object passwordO = request.getParameter("password");
			AvailableDatabase availableDatabase = databaseO != null ? AvailableDatabase.getAvailableDatabase( Integer.valueOf( (String) databaseO ) ) : null;
			String host = hostO != null ? (String) hostO : null;
			String user = userO != null ? (String) userO : null;
			String password = passwordO != null ? (String) passwordO : null;
			MonitorInfo monitorInfo = new MonitorInfo();
			monitorInfo.setDatabase(availableDatabase);
			monitorInfo.setHost(host);
			monitorInfo.setUser(user);
			monitorInfo.setPassword(password);
			try {
				ConnectionPool.getInstance().openConnection( monitorInfo );
				pw.write("{ \"sucesso\" : true , \"msg\" : \"Conexão aberta com sucesso\" }");
			} catch (ClassNotFoundException e) {
				pw.write("{ \"sucesso\" : false , \"msg\" : \"Drive de conexão não encontrado.\" }");
				e.printStackTrace();
			} catch (SQLException e) {
				pw.write("{ \"sucesso\" : false , \"msg\" : \"Não foi possivel abrir a conexão, verifique as informações de conexão.\" }");
				e.printStackTrace();
			}
			return;
		}

		if (UPDATE.equalsIgnoreCase(action)) {
			Object hostO = request.getParameter("host");
			String host = hostO != null ? (String) hostO : null;
			Object consultaO = request.getParameter("consulta");
			String consulta = consultaO != null ? (String) consultaO : null;
			if ((consulta != null) && (host != null))
				try {
					Connection conn = ConnectionPool.getInstance().getConnection(host);
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(consulta);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"dd/MM/yyyy as HH:mm:ss");
					pw.write("{ \"sucesso\" : true , \"msg\" : \"Consulta realizada com sucesso\" , \"result\" : "
							+ convertResultSetToJSON(rs)
							+ " , \"dataAtualizacao\" : \""
							+ sdf.format(new Date()) + "\" }");
				} catch (SQLException e) {
					pw.write("{ \"sucesso\" : false , \"msg\" : \"Não foi possivel realizar a consulta, verifiquer os dados da consulta.\" }");
					e.printStackTrace();
				}
			else
				pw.write("{ \"sucesso\" : true , \"msg\" : \"Não a nenhuma consulta a ser realizada\" }");
			
			return;
		}
		
		pw.write("{ \"sucesso\" : true , \"msg\" : \"This is not a valid action\" }");
	}

	private String convertResultSetToJSON(ResultSet rs) throws SQLException {
		int colCount = rs.getMetaData().getColumnCount();
		StringBuilder sb = new StringBuilder("{ \"rows\" : [");
//		int total = rs.getRow();
		List<String> arrayList = new ArrayList<String>();
		StringBuilder sb2 = null;
		while (rs.next()) {
			sb2 = new StringBuilder("{ ");
			for (int i = 1; i <= colCount; i++) {
				sb2.append(String.format("\"%s\" : \"%s\" ",
						new Object[] { rs.getMetaData().getColumnLabel(i),
								String.valueOf(rs.getObject(i)) }));
				if (i + 1 <= colCount) {
					sb2.append(" , ");
				}
			}
			sb2.append("}");
			arrayList.add(sb2.toString());
		}
		for (int i = 0; i < arrayList.size(); i++) {
			sb.append((String) arrayList.get(i));
			if (i + 1 < arrayList.size()) {
				sb.append(" , ");
			}
		}
		sb.append(" ] }");
		return sb.toString();
	}

}
