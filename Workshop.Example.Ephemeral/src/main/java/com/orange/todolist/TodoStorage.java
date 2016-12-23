package com.orange.todolist;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orange.todolist.exception.BusinessConcurrencyException;
import com.orange.todolist.exception.CancellationException;

/**
 * Classe responsable de stoquer la liste des taches rafraichie via {@link #put(String)}
 */
public abstract class TodoStorage {
	public static final Logger logger = LoggerFactory.getLogger(TodoStorage.class);

	
	private void validate(String todosAsString){
		logger.debug("Checking [{}]",todosAsString);
		if(todosAsString.matches(".*([Rr].silier.*[Oo]range).*")  || todosAsString.matches(".*([Cc]ancel.*[Oo]range).*") ){
			throw new CancellationException("you can not cancel an orange service");
		}
		if(todosAsString.matches(".*([fF]ree|[Ss][Ff][Rr]|[Bb]ouygues).*")){
			throw new BusinessConcurrencyException("tasks related to orange competitors are not allowed");
		}
	}
	
	public void put(String todosAsString) throws IOException{
		validate(todosAsString);
		JSONTokener tokener = new JSONTokener(todosAsString);
		try {
			doPut((JSONArray) tokener.nextValue());
		} catch (JSONException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Met a jour la liste des tâches
	 * @param todosAsString La liste des tâches sous la formes d'un tableau JSON serialisé 
	 */
	public abstract void doPut(JSONArray todosAsString) throws IOException;
	
	/**
	 * @return les taches sous la forme d'un {@link JSONArray}
	 */
	public abstract JSONArray get() throws IOException ;

}
