package com.panduit.servergraph.data;

public class Edge {
	private String label;
	private Double weight;
	private Vertex start;
	private Vertex end;
	private boolean dualDirection;

	public Edge(String label, Double weight, Vertex start, Vertex end, boolean dualDirection) {
		this.label = label;
		this.weight = weight;
		this.start = start;
		this.end = end;
		this.dualDirection = dualDirection;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Vertex getStart() {
		return start;
	}

	public void setStart(Vertex start) {
		this.start = start;
	}

	public Vertex getEnd() {
		return end;
	}

	public void setEnd(Vertex end) {
		this.end = end;
	}
	
	
	public boolean isDualDirection() {
		return dualDirection;
	}

	public void setDualDirection(boolean dualDirection) {
		this.dualDirection = dualDirection;
	}
}
