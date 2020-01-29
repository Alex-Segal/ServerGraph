package com.panduit.servergraph.util;

import java.util.List;
import java.util.Map;

import com.panduit.servergraph.data.Edge;
import com.panduit.servergraph.data.Graph;
import com.panduit.servergraph.data.Vertex;

public interface GraphStorage {
	public void storeGraph (String location, Graph graph);
	public Map<Vertex, List<Edge>> createGrapgFromStorage (String location);
}
