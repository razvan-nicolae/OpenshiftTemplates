package com.orange.todolist;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Unique servlet responsable de répondre aux GET/POST HTTP sur /todos
 */
public class TodosServlet extends HttpServlet {
	
	public TodoStorage getStorageService(){
		//In Memory storage
//		return new TodoStorageInMemory();
		
		//Use Mysql on Kermit
		String login = System.getenv("MYSQL_USER");
		String password = System.getenv("MYSQL_PASSWORD");
		String host = System.getenv("MYSQL_SERVICE_HOST");
		String port = System.getenv("MYSQL_SERVICE_PORT");
		String database = System.getenv("MYSQL_DATABASE");
		return new TodoStorageJDBCMysql(host, port, database, login, password);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String todosAsString = req.getParameter("todos");
		getStorageService().put(todosAsString);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE");
		resp.setHeader("Access-Control-Allow-Headers", "X-Requested-With,content-type");
		resp.setHeader("Access-Control-Allow-Credentials", "true");
		
		PrintWriter writer = resp.getWriter();
		writer.write(getStorageService().get().toString());
		writer.close();
	}
	
}
