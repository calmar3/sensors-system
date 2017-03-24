package rest;


import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class DTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonInclude(Include.NON_NULL)
	private String data;
	@JsonInclude(Include.NON_NULL)
	protected String error;
	
	public DTO(){};
	
	public DTO( String data ) {
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
}
