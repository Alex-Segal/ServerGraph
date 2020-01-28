import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import com.panduit.servergraph.data.Edge;
import com.panduit.servergraph.data.Graph;
import com.panduit.servergraph.data.Vertex;

public class GraphTest {
	
	Graph graph = Graph.getInstance();
	
	@Before
	public void init() {
		graph.getAdjVertices().clear();
		graph.getEdges().clear();
    }
	
	private List<Edge> getEdgesForVertex(String vertexLabel) {
		return graph.getAdjVertices().get(vertexLabel).get(graph.extractVertex(vertexLabel));
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
		assertNotNull(graph.getAdjVertices().get("Server2").get(graph.extractVertex("Server2")).get(0));
		assertThat(getEdgesForVertex("Server2").get(0).getWeight(), Is.is(5.6));
		assertThat(getEdgesForVertex("Server2").get(0).getLabel(), Is.is("link1"));
		assertThat(getEdgesForVertex("Server2").get(0).getStart(), Is.is(graph.extractVertex("Server1")));
		assertThat(getEdgesForVertex("Server2").get(0).getEnd(), Is.is(graph.extractVertex("Server2")));
	}
	
	@Test
	public void addDualDirectedEdgeTest() {
		graph.addVertex("Server3");
		graph.addVertex("Server4");
		graph.addDualDirectedEdge("link1", 5.6, "Server3", "Server4", true);
		assertNotNull(getEdgesForVertex("Server3"));
		assertThat(getEdgesForVertex("Server3").get(0).getWeight(), Is.is(5.6));
		assertThat(getEdgesForVertex("Server3").get(0).getLabel(), Is.is("link1"));
		assertThat(getEdgesForVertex("Server3").get(0).getStart(), Is.is(graph.extractVertex("Server3")));
		assertThat(getEdgesForVertex("Server3").get(0).getEnd(), Is.is(graph.extractVertex("Server4")));
		assertTrue(getEdgesForVertex("Server3").get(0).isDualDirection());
		
		assertNotNull(getEdgesForVertex("Server4"));
		assertThat(getEdgesForVertex("Server4").get(0).getWeight(), Is.is(5.6));
		assertThat(getEdgesForVertex("Server4").get(0).getLabel(), Is.is("link1"));
		assertThat(getEdgesForVertex("Server4").get(0).getStart(), Is.is(graph.extractVertex("Server3")));
		assertThat(getEdgesForVertex("Server4").get(0).getEnd(), Is.is(graph.extractVertex("Server4")));
		assertTrue(getEdgesForVertex("Server4").get(0).isDualDirection());
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
		
		assertNotNull(getEdgesForVertex("Server1"));
		assertNotNull(getEdgesForVertex("Server2"));
		
		graph.removeEdgeByVertices("Server1", "Server2");
		assertThat(getEdgesForVertex("Server1").size(), Is.is(0));
		assertThat(getEdgesForVertex("Server2").size(), Is.is(0));
	}
	
	@Test
	public void getConnectionsForVetrexTest() {
		graph.addVertex("Server1");
		graph.addVertex("Server2");
		graph.addVertex("Server3");
		graph.addVertex("Server4");
		graph.addDirectedEdge("link1", 5.6, "Server1", "Server2");
		graph.addDualDirectedEdge("link1", 5.6, "Server3", "Server4", true);
		graph.addDualDirectedEdge("link1", 5.6, "Server3", "Server2", true);
		assertThat(graph.getConnectionsForVetrex("Server1").size(), Is.is(0));
		assertThat(graph.getConnectionsForVetrex("Server2").size(), Is.is(2));
		assertThat(graph.getConnectionsForVetrex("Server3").size(), Is.is(2));
		assertThat(graph.getConnectionsForVetrex("Server4").size(), Is.is(1));
	}

	
	
	

}
