package com.orange.todolist;

import org.json.JSONArray;

public class TodoStorageInMemory extends TodoStorage{

	private static JSONArray TODOS = new JSONArray();
	
	/**
	 * Met a jour la liste des tâches
	 * @param todos La liste des tâches sous la formes d'un tableau JSON serialisé 
	 */
	public void doPut(JSONArray todos) {
		TODOS = todos;
	}
	
	/**
	 * @return les taches sous la forme d'un {@link JSONArray}
	 */
	public JSONArray get(){
		logger.debug("Requesting todos : {}", TODOS);
		return TODOS;
	}
	
}
