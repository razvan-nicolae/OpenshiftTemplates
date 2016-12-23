package com.orange.todolist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;


public class TodoStorageJDBCMysql extends TodoStorage {
	
	private String login;
	private String password;
	private String host;
	private String port;
	private String database;

	public TodoStorageJDBCMysql(String host, String port, String database, String login, String password){
		this.host = host;
		this.port = port;
		this.database = database;
		this.login = login;
		this.password = password;
	}

	@Override
	public void doPut(JSONArray todos) throws IOException {
		Connection connect = connect();
		try {
			Statement stmt = connect.createStatement();
			stmt.execute("delete from todos where tasks is not null");
			PreparedStatement insert = connect.prepareStatement("insert into todos values (?)");
			insert.setString(1, todos.toString());
			insert.execute();
		} catch (SQLException e) {
			throw new IOException(e);
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
				logger.error("Error closing connection", e);
			}
		}
	}

	@Override
	public JSONArray get() throws IOException {
		Connection connect = connect();
		try {
			Statement stmt = connect.createStatement();
			ResultSet resultSet = stmt.executeQuery("select * from todos");
			if(resultSet.next()){
				return new JSONArray(resultSet.getString("tasks"));
			}else{
				return new JSONArray();
			}
		} catch (SQLException e) {
			throw new IOException(e);
		} catch (JSONException e) {
			throw new IOException(e);
		}finally{
			try {
				connect.close();
			} catch (SQLException e) {
				logger.error("Error closing connection", e);
			}
		}
	}
	
	private Connection connect() throws IOException{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, login, password);
			Statement stmt = connect.createStatement();
			boolean creation =  stmt.execute("CREATE TABLE IF NOT EXISTS todos (tasks VARCHAR(500))");
			logger.debug("Create table result {}", creation);
			return connect;
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}
}
