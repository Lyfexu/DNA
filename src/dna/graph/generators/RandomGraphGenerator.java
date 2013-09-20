package dna.graph.generators;

import dna.graph.Graph;
import dna.graph.datastructures.GraphDataStructure;
import dna.graph.edges.Edge;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.IRandomGenerator;
import dna.graph.nodes.Node;
import dna.util.Rand;
import dna.util.parameters.Parameter;

public class RandomGraphGenerator extends GraphGenerator implements
		IRandomGenerator {
	public RandomGraphGenerator(String name, Parameter[] params,
			GraphDataStructure gds, long timestampInit, int nodesInit,
			int edgesInit) {
		super(name, params, gds, timestampInit, nodesInit, edgesInit);
	}

	@Override
	public Graph generate() {
		Graph graph = this.newGraphInstance();

		for (int i = 0; i < this.nodesInit; i++) {
			Node node = this.gds.newNodeInstance(i);
			graph.addNode(node);
		}

		while (graph.getEdgeCount() < this.edgesInit) {
			int src = Rand.rand.nextInt(graph.getNodeCount());
			int dst = Rand.rand.nextInt(graph.getNodeCount());
			if (src != dst) {
				Edge edge = this.gds.newEdgeInstance(graph.getNode(src),
						graph.getNode(dst));
				graph.addEdge(edge);
				edge.connectToNodes();
			}
		}

		return graph;
	}

}