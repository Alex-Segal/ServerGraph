package com.panduit.servergraph.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panduit.servergraph.data.Edge;
import com.panduit.servergraph.data.Graph;
import com.panduit.servergraph.data.Vertex;

public class GraphStorageJsonImpl implements GraphStorage{
	ObjectMapper mapper = new ObjectMapper();

	public void storeGraph(String location, Graph graph) {
		File file = new File(location);
		try {
            // Serialize Java object info JSON file.
            mapper.writeValue(file, graph.getAdjVertices());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public Map<Vertex, List<Edge>> createGrapgFromStorage (String location){
		Map<Vertex, List<Edge>> graphMap = new HashMap<Vertex, List<Edge>>();
		try {
			File file = new File(location);
            // Deserialize JSON file into Java object.
			Map<String, List<Edge>> fileMap = mapper.readValue(file, Map.class);
			for (Entry<String, List<Edge>> entry : fileMap.entrySet()) {
				graphMap.put(new Vertex(entry.getKey()), entry.getValue());
			}
			
        } catch (IOException e) {
            e.printStackTrace();
        }
		return graphMap;
	}
	

}
