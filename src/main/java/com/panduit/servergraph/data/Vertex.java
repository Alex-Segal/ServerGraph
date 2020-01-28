package com.panduit.servergraph.data;

public class Vertex {
	String label;

	public Vertex(String label) {
        this.label = label;
    }
	
	
	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	@Override
    public boolean equals(Object obj) {
		boolean isEqual = false;
	    if(this == obj) 
	    	isEqual = true;
	    
	    return isEqual;
    } 
}
