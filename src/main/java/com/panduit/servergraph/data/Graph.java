package com.panduit.servergraph.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.google.common.base.Strings;


// assumptions:
// 1. This Graph operates under the assumption that edges can be added only between existing vertices.
// 2. Edges cannot exist without vertices (no connections if there are no nodes to connect).

public class  Graph {
	private static final Graph instance = new Graph();
	private Map<String, Map<Vertex, List<Edge>>> adjVertices = new ConcurrentHashMap<String, Map<Vertex, List<Edge>>>();
	
	// If a little more memory is not an issue, this map will help to keep truck on edges by name.
	// This will help retrieving edges by label without looping through the whole graph.
	private Map<String, List<String>> edges = new ConcurrentHashMap<String, List<String>>();
	
	
//	private Map<Vertex, List<Edge>> adjVertices = new ConcurrentHashMap<Vertex, List<Edge>>();
//	private ConcurrentHashMap<String, Vertex> vertices = new ConcurrentHashMap<String, Vertex>();
//	private ConcurrentHashMap<String, Edge> edges = new ConcurrentHashMap<String, Edge>();
	
	private Graph() {
	}
	
	public static Graph getInstance() {
		return instance;
	}

	public synchronized Map<String, Map<Vertex, List<Edge>>> getAdjVertices() {
		return adjVertices;
	}

	public synchronized void setAdjVertices(Map<String, Map<Vertex, List<Edge>>> adjVertices) {
		this.adjVertices = adjVertices;
	}

	public Map<String, List<String>> getEdges() {
		return edges;
	}

	public void setEdges(Map<String, List<String>> edges) {
		this.edges = edges;
	}
	
	public Vertex extractVertex(String label) {
		Map.Entry<Vertex, List<Edge>> entry = getAdjVertices().get(label).entrySet().iterator().next();
		return entry.getKey();
	}

	public void addVertex(String label) {		
		Vertex vertex = new Vertex(label);
		Map<Vertex, List<Edge>> vertexMap = new HashMap<Vertex, List<Edge>>();
		vertexMap.put(vertex, new ArrayList<Edge>());
		getAdjVertices().putIfAbsent(label, vertexMap);
	}
	
	// In case of directed edge, the end node register with the edge because it is the one who get the relationship towards
	public void addDirectedEdge(String label, Double weight, String start, String end) {
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
		getAdjVertices().get(end).get(extractVertex(end)).add(edge);
		edges.put(label, Arrays.asList(vertex2.getLabel()));
	}
	
	public void addDualDirectedEdge(String label, Double weight, String start, String end, boolean dualDirection) {
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
		getAdjVertices().get(start).get(extractVertex(start)).add(edge);
		getAdjVertices().get(end).get(extractVertex(end)).add(edge);
		edges.put(label, Arrays.asList(vertex1.getLabel(), vertex2.getLabel()));
	}
	
	public void removeVertex(String label) {
		getAdjVertices().remove(label);
	}
	
	public void removeEdgeByVertices(String lab1, String lab2) {
		Map<Vertex, List<Edge>> verMap1 = getAdjVertices().get(lab1);
		if (verMap1 != null) {
			List<Edge> edges = verMap1.get(extractVertex(lab1));
			edges.removeIf(edge -> edge.getStart().getLabel().equals(lab1) && edge.getEnd().getLabel().equals(lab2));
		}
		Map<Vertex, List<Edge>> verMap2 = getAdjVertices().get(lab2);
		if (verMap2 != null) {
			List<Edge> edges = verMap2.get(extractVertex(lab2));
			edges.removeIf(edge -> edge.getStart().getLabel().equals(lab1) && edge.getEnd().getLabel().equals(lab2));
		}
	}
	
	public void removeEdgeByLabel(String label) {
		List<String> vertices = edges.get(label);
		vertices.forEach(v -> {
			List<Edge> edge = getAdjVertices().get(v).get(v);
			edge.forEach(e -> edge.remove(e));
		});

	}
	
	public List<Vertex> getConnectionsForVetrex(String label){
		List<Vertex> vertices = new ArrayList<Vertex>();
		List<Edge> edge = getAdjVertices().get(label).get(extractVertex(label));
		edge.forEach(v -> vertices.add(v.getEnd()));
		return vertices;
	}
}
