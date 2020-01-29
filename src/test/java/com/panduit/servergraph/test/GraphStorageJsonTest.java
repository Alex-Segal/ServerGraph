package com.panduit.servergraph.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import com.panduit.servergraph.data.Edge;
import com.panduit.servergraph.data.Graph;
import com.panduit.servergraph.data.Vertex;
import com.panduit.servergraph.util.GraphStorageJsonImpl;

public class GraphStorageJsonTest {
	
	@Test
	public void storeGraphTest() throws IOException {
		Graph graph = Graph.getInstance();
		graph.addVertex("Server1");
		graph.addVertex("Server2");
		graph.addDirectedEdge("link1", 5.6, "Server1", "Server2");
		
		String file = "GraphFiles/graph.json";
		Path path = Paths.get(file);
		Files.deleteIfExists(path);
		
		GraphStorageJsonImpl gsj = new GraphStorageJsonImpl();
		gsj.storeGraph(file, graph);

		
		Map<Vertex, List<Edge>> graphMap = gsj.createGrapgFromStorage(file);
		
		List<Vertex> vertecis = graphMap.keySet().stream().collect(Collectors.toList());
		assertTrue(vertecis.size() == 2);
		Vertex vertex1  = vertecis.stream().filter(v -> v.getLabel().equals("Server1")).collect(Collectors.toList()).get(0);
		// Edges needs to be reformated into proper obj because json reader do not read it as List<Edge>
		assertTrue(graphMap.get(vertex1).toString().contains("link1"));
	}

}
