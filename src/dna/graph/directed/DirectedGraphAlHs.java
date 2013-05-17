package dna.graph.directed;

import dna.graph.GraphDatastructures;
import dna.graph.edges.EdgesHs;
import dna.graph.nodes.NodesAl;

public class DirectedGraphAlHs extends DirectedGraph {

	public DirectedGraphAlHs(String name, long timestamp,
			GraphDatastructures<DirectedGraph, DirectedNode, DirectedEdge> ds,
			int nodes, int edges) {
		super(name, timestamp, ds, new NodesAl<DirectedNode, DirectedEdge>(
				nodes), new EdgesHs<DirectedEdge>(edges));
	}

}