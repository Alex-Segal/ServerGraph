package com.panduit.servergraph.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.panduit.servergraph.data.Edge;
import com.panduit.servergraph.data.Graph;
import com.panduit.servergraph.data.Vertex;
import com.panduit.servergraph.util.DijkstraAlgorithm;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DijkstraAlgorithmTest {

    private Map<String, Vertex> nodes;
    Graph graph = Graph.getInstance();
    @Before
	public void init() {
		graph.getAdjVertices().clear();
		graph.getEdges().clear();
		nodes = new HashMap<String, Vertex>();
		for (int i = 0; i < 10; i++) {
            Vertex vertex = new Vertex("Server" + i);
            graph.addVertex(vertex.getLabel());
        }
		for (Vertex vertex : graph.getAllVertices()) {
        	nodes.put(vertex.getLabel(), vertex);
        }
    }
    
    @Test
    public void singlePathTest() {
        graph.addDirectedEdge("Edge0", 1.5, "Server0", "Server1");
        graph.addDirectedEdge("Edge1", 1.5, "Server1", "Server2");
        graph.addDirectedEdge("Edge2", 1.5, "Server2", "Server3");

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get("Server0"));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get("Server3"));

        assertNotNull(path);
        // return 4 nodes including start point
        assertTrue(path.size() == 4);
        // check each server in path
        for (int i = 0; i < path.size(); i++) {
        	assertTrue(path.get(i).getLabel().equals("Server" + i));
        }
    }
    
    @Test
    public void multiPathTest() {
    	graph.addDirectedEdge("Edge0", 1.5, "Server0", "Server1");
        graph.addDirectedEdge("Edge1", 1.5, "Server1", "Server2");
        graph.addDirectedEdge("Edge2", 1.5, "Server2", "Server3");
        graph.addDirectedEdge("Edge3", 1.0, "Server0", "Server3");

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get("Server0"));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get("Server3"));

        assertNotNull(path);
        // return 4 nodes including start point
        assertTrue(path.size() == 2);
        // check each server in path
        assertTrue(path.get(0).getLabel().equals("Server0"));
        assertTrue(path.get(1).getLabel().equals("Server3"));
    }
    
    @Test
    public void multiPath2Test() {
    	graph.addDirectedEdge("Edge0", 1.5, "Server0", "Server1");
        graph.addDirectedEdge("Edge1", 1.5, "Server1", "Server2");
        graph.addDirectedEdge("Edge2", 1.5, "Server2", "Server3");
        graph.addDirectedEdge("Edge3", 6.0, "Server0", "Server3");

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get("Server0"));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get("Server3"));

        assertNotNull(path);
        // return 4 nodes including start point
        assertTrue(path.size() == 4);
        // check each server in path
        for (int i = 0; i < path.size(); i++) {
        	assertTrue(path.get(i).getLabel().equals("Server" + i));
        }
    }
    
    
    
    
    
}