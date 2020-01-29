package com.panduit.servergraph.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import com.panduit.servergraph.data.Graph;
import com.panduit.servergraph.data.Vertex;

public class GraphTest {
	
	Graph graph = Graph.getInstance();
	
	@Before
	public void init() {
		graph.getAdjVertices().clear();
		graph.getEdges().clear();
    }
	
	@Test
	public void vertexEqualsOverwriteTest () {
		Vertex v1 = new Vertex("leb1");
		Vertex v2 = new Vertex("leb1");
		assertEquals(v1, v2);
	}
	
	@Test
	public void getAllVerticesTest() {
		graph.addVertex("Server1");
		graph.addVertex("Server2");
		graph.addVertex("Server6");
		graph.addVertex("Server9");
		assertThat(graph.getAdjVertices().size(), Is.is(4));
	}
	
	@Test
	public void getAllEdges() {
		graph.addVertex("Server1");
		graph.addVertex("Server2");
		graph.addVertex("Server3");
		graph.addVertex("Server4");
		graph.addDirectedEdge("link0", 5.6, "Server1", "Server2");
		graph.addDualDirectedEdge("link1", 5.6, "Server3", "Server4", true);
		graph.addDualDirectedEdge("link2", 5.6, "Server3", "Server2", true);
		assertThat(graph.getAllEdges().size(), Is.is(3));
		
	}
	
	@Test
	public void addVertexTest() {
		graph.addVertex("Server1");
		Vertex vertex = graph.extractVertex("Server1");
		assertNotNull(vertex);
		assertThat(vertex.getLabel(), Is.is("Server1"));
	}
	
	@Test
	public void addDirectedEdgeTest() {
		graph.addVertex("Server1");
		graph.addVertex("Server2");
		graph.addDirectedEdge("link1", 5.6, "Server1", "Server2");
		assertNotNull(graph.getAdjVertices().get(graph.extractVertex("Server2")));
		assertThat(graph.getEdgesForVertex("Server1").get(0).getWeight(), Is.is(5.6));
		assertThat(graph.getEdgesForVertex("Server1").get(0).getLabel(), Is.is("link1"));
		assertThat(graph.getEdgesForVertex("Server1").get(0).getStart(), Is.is(graph.extractVertex("Server1")));
		assertThat(graph.getEdgesForVertex("Server1").get(0).getEnd(), Is.is(graph.extractVertex("Server2")));
	}
		
	@Test
	public void addDualDirectedEdgeTest() {
		graph.addVertex("Server3");
		graph.addVertex("Server4");
		graph.addDualDirectedEdge("link1", 5.6, "Server3", "Server4", true);
		assertNotNull(graph.getEdgesForVertex("Server3"));
		assertThat(graph.getEdgesForVertex("Server3").get(0).getWeight(), Is.is(5.6));
		assertThat(graph.getEdgesForVertex("Server3").get(0).getLabel(), Is.is("link1"));
		assertThat(graph.getEdgesForVertex("Server3").get(0).getStart(), Is.is(graph.extractVertex("Server3")));
		assertThat(graph.getEdgesForVertex("Server3").get(0).getEnd(), Is.is(graph.extractVertex("Server4")));
		assertTrue(graph.getEdgesForVertex("Server3").get(0).isDualDirection());
		
		assertNotNull(graph.getEdgesForVertex("Server4"));
		assertThat(graph.getEdgesForVertex("Server4").get(0).getWeight(), Is.is(5.6));
		assertThat(graph.getEdgesForVertex("Server4").get(0).getLabel(), Is.is("link1"));
		assertThat(graph.getEdgesForVertex("Server4").get(0).getStart(), Is.is(graph.extractVertex("Server3")));
		assertThat(graph.getEdgesForVertex("Server4").get(0).getEnd(), Is.is(graph.extractVertex("Server4")));
		assertTrue(graph.getEdgesForVertex("Server4").get(0).isDualDirection());
	}
		
	@Test
	public void removeVertexTest() {
		graph.addVertex("Server1");
		graph.removeVertex("Server1");
		assertNull(graph.getAdjVertices().get("Server2"));
	}
	
	@Test
	public void removeEdgeByVerticesTest() {
		graph.addVertex("Server1");
		graph.addVertex("Server2");
		graph.addDualDirectedEdge("link1", 5.6, "Server1", "Server2", true);	
		
		assertNotNull(graph.getEdgesForVertex("Server1"));
		assertNotNull(graph.getEdgesForVertex("Server2"));
		
		graph.removeEdgeByVertices("Server1", "Server2");
		assertThat(graph.getEdgesForVertex("Server1").size(), Is.is(0));
		assertThat(graph.getEdgesForVertex("Server2").size(), Is.is(0));
	}
	
	@Test
	public void removeEdgeByLableTest() {
		graph.addVertex("Server1");
		graph.addVertex("Server2");
		graph.addDualDirectedEdge("link1", 5.6, "Server1", "Server2", true);
		
		assertNotNull(graph.getEdgesForVertex("Server1"));
		assertNotNull(graph.getEdgesForVertex("Server2"));
		
		graph.removeEdgeByLabel("link1");
		assertThat(graph.getEdgesForVertex("Server1").size(), Is.is(0));
		assertThat(graph.getEdgesForVertex("Server2").size(), Is.is(0));
	}
	
	@Test
	public void getConnectionsForVetrexTest() {
		graph.addVertex("Server1");
		graph.addVertex("Server2");
		graph.addVertex("Server3");
		graph.addVertex("Server4");
		graph.addDirectedEdge("link1", 5.6, "Server1", "Server2");
		graph.addDualDirectedEdge("link2", 5.6, "Server3", "Server4", true);
		graph.addDualDirectedEdge("link3", 5.6, "Server3", "Server2", true);
		assertThat(graph.getConnectionsForVetrex("Server1").size(), Is.is(1));
		assertThat(graph.getConnectionsForVetrex("Server2").size(), Is.is(1));
		assertThat(graph.getConnectionsForVetrex("Server3").size(), Is.is(2));
		assertThat(graph.getConnectionsForVetrex("Server4").size(), Is.is(1));
	}

	
	

}
