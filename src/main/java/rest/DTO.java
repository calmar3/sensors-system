package rest;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class DTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonInclude(Include.NON_NULL)
	protected String error;
	
	public DTO(){};
	
	public DTO(String error){
		this.error=error;
	}
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	@JsonInclude(Include.NON_NULL)
	protected String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
