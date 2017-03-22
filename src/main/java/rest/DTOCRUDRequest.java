package rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class DTOCRUDRequest {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	@JsonInclude(Include.NON_NULL)
	private String data;

	public DTOCRUDRequest() {}
	
	public DTOCRUDRequest( String data ) {
		super();
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
}
