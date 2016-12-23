package com.orange.todolist.exception;

public class BusinessConcurrencyException extends RuntimeException {

	public BusinessConcurrencyException(String message) {
		super(message);
	}
	
}
