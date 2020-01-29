package com.panduit.servergraph.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.google.common.base.Strings;


// assumptions:
// 1. This Graph operates under the assumption that edges can be added only between existing vertices.
// 2. Edges cannot exist without vertices (no connections if there are no nodes to connect).
// 3. If you enter second edge with the same label, it will overwrite (delete old) the previouse one

public class  Graph {
	private static Graph instance;
	private static Map<Vertex, List<Edge>> adjVertices = new ConcurrentHashMap<Vertex, List<Edge>>();
	private static final Logger log = LogManager.getLogger(Graph.class);
	
	
	// If a little more memory is not an issue, this map will help to keep truck on edges by name.
	// This will help retrieving edges by label without looping through the whole graph.
	private Map<String, Edge> edges = new ConcurrentHashMap<String, Edge>();
	
	private Graph() {
		PropertyConfigurator.configure("src/main/resources/log4j.properties");
	}
	
	public static Graph getInstance() {
		return (instance == null) ? new Graph() : instance;
	}

	public synchronized Map<Vertex, List<Edge>> getAdjVertices() {
		return adjVertices;
	}

	public synchronized void setAdjVertices(Map<Vertex, List<Edge>> adjVertices) {
		this.adjVertices = adjVertices;
	}

	public Map<String, Edge> getEdges() {
		return edges;
	}

	public void setEdges(Map<String, Edge> edges) {
		this.edges = edges;
	}
	
	public Vertex extractVertex(String label) {		
		for (Entry<Vertex, List<Edge>> entry : getAdjVertices().entrySet()) {
	        if (entry.getKey().getLabel().equals(label)) {
	            return entry.getKey();
	        }
	    }
		return null;
	}
	
	public List<Edge> getEdgesForVertex(String vertexLabel) {
		return getAdjVertices().get(extractVertex(vertexLabel));
	}
	
	public List<Vertex> getAllVertices() {
		List<Vertex> vertices = new ArrayList<Vertex>();
		for (Entry<Vertex, List<Edge>> entry : getAdjVertices().entrySet()) {
			vertices.add(entry.getKey());
		}
		return vertices;
	}
	
	public List<Edge> getAllEdges() {
		List<Edge> allEdges = new ArrayList<Edge>();
		for (Entry<String, Edge> entry : edges.entrySet()) {
			allEdges.add(entry.getValue());
		}
		return allEdges;
	}
	
	public int getVertecisCount() {
		return getAdjVertices().size();
	}

	public void addVertex(String label) {		
		Vertex vertex = new Vertex(label);
		getAdjVertices().putIfAbsent(vertex, new ArrayList<Edge>());
		log.info("Added Vertex: " + label);
	}
	
	// In case of directed edge, the start node register with the edge because it is the origin of the connection
	public void addDirectedEdge(String label, Double weight, String start, String end) {
		// In case an edge will be entered with a lable that already exist, it will overwrite the old one.
		if(edges.get(label) != null) {
			removeEdgeByLabel(label);
		}
		Vertex vertex1 = extractVertex(start);
		Vertex vertex2 = extractVertex(end);
		// default label
		if (Strings.isNullOrEmpty(label)) {
			label = vertex1.getLabel()+vertex2.getLabel();
		}
		// default weight if not present to 1
		if (weight == null) {
			weight = 1.0;
		}
		Edge edge = new Edge(label, weight, vertex1, vertex2, false);
		getAdjVertices().get(vertex1).add(edge);
		edges.put(label, edge);
		log.info("Added Directed Edge: " + label);
	}
	
	public void addDualDirectedEdge(String label, Double weight, String start, String end, boolean dualDirection) {
		// In case an edge will be entered with a lable that already exist, it will overwrite the old one.
		if(edges.get(label) != null) {
			removeEdgeByLabel(label);
		}
		Vertex vertex1 = extractVertex(start);
		Vertex vertex2 = extractVertex(end);
		if (Strings.isNullOrEmpty(label)) {
			label = vertex1.getLabel()+vertex2.getLabel();
		}
		// default weight if not present to 1
		if (weight == null) {
			weight = 1.0;
		}
		Edge edge = new Edge(label, weight, vertex1, vertex2, dualDirection);
		getAdjVertices().get(vertex1).add(edge);
		getAdjVertices().get(vertex2).add(edge);
		edges.put(label, edge);
		log.info("Added Dual Directed Edge: " + label);
	}
	
	public void removeVertex(String label) {
		getAdjVertices().remove(label);
		log.info("Removed Vertex: " + label);
	}
	
	public void removeEdgeByVertices(String lab1, String lab2) {
		Map<Vertex, List<Edge>> verMap1 = getAdjVertices();
		if (verMap1 != null) {
			List<Edge> edges = verMap1.get(extractVertex(lab1));
			edges.removeIf(edge -> edge.getStart().getLabel().equals(lab1) && edge.getEnd().getLabel().equals(lab2));
		}
		Map<Vertex, List<Edge>> verMap2 = getAdjVertices();
		if (verMap2 != null) {
			List<Edge> edges = verMap2.get(extractVertex(lab2));
			edges.removeIf(edge -> edge.getStart().getLabel().equals(lab1) && edge.getEnd().getLabel().equals(lab2));
		}
		log.info("Removed Edge from Verticies: " + lab1 + " and " + lab2);
	}
	
	public void removeEdgeByLabel(String label) {
		Edge edgeRef = edges.get(label);
		List<String> vertices = new ArrayList<String>(); 
		vertices.add(edgeRef.getStart().getLabel());
		vertices.add(edgeRef.getEnd().getLabel());
		for (String vertex : vertices) {
			List<Edge> edges = getAdjVertices().get(extractVertex(vertex));
			
			// To prevent ConcurrentModificationException clone the obj
			List<Edge> clonedEdges = new ArrayList<Edge>(edges);			
			for (Edge e : clonedEdges) {
				if (e.getLabel() == label) {
					edges.remove(e);
				}
			}
		}
		log.info("Removed Edge: " + label);
	}
	
	public List<Vertex> getConnectionsForVetrex(String label){
		List<Vertex> vertices = new ArrayList<Vertex>();
		Vertex vertex = extractVertex(label);
		List<Edge> edge = getAdjVertices().get(vertex);
		edge.forEach(v -> vertices.add(v.getEnd()));
		return vertices;
	}
}
