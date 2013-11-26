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

@WebServlet({ "/monitor" })
public class MonitorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String INICIAR = "iniciar";
	private static final String ATUALIZAR = "atualizar";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String acao = request.getParameter("acao") != null ? request
				.getParameter("acao") : null;

		if ("iniciar".equalsIgnoreCase(acao)) {
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();

			Object hostO = request.getParameter("host");
			Object usuarioO = request.getParameter("usuario");
			Object passwordO = request.getParameter("password");
			String host = hostO != null ? (String) hostO : null;
			String usuario = usuarioO != null ? (String) usuarioO : null;
			String password = passwordO != null ? (String) passwordO : null;
			try {
				ConnectionPool.openConnection(host, usuario, password);
				pw.write("{ \"sucesso\" : true , \"msg\" : \"Conexão aberta com sucesso\" }");
			} catch (ClassNotFoundException e) {
				pw.write("{ \"sucesso\" : false , \"msg\" : \"Drive de conexão não encontrado.\" }");
				e.printStackTrace();
			} catch (SQLException e) {
				pw.write("{ \"sucesso\" : false , \"msg\" : \"Não foi possivel abrir a conexão, verifique as informações de conexão.\" }");
				e.printStackTrace();
			}
		}

		if ("atualizar".equalsIgnoreCase(acao)) {
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();

			Object hostO = request.getParameter("host");
			String host = hostO != null ? (String) hostO : null;
			Object consultaO = request.getParameter("consulta");
			String consulta = consultaO != null ? (String) consultaO : null;
			if ((consulta != null) && (host != null))
				try {
					Connection conn = ConnectionPool.getConnection(host);
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
		}
	}

	private String convertResultSetToJSON(ResultSet rs) throws SQLException {
		int colCount = rs.getMetaData().getColumnCount();
		StringBuilder sb = new StringBuilder("{ \"rows\" : [");
		int total = rs.getRow();
		List arrayList = new ArrayList();
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
