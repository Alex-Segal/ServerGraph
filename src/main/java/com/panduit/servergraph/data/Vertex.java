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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return  label;
    }
}
