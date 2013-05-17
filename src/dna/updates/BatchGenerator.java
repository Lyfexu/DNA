package dna.updates;

import dna.graph.Edge;
import dna.graph.Graph;
import dna.graph.GraphDatastructures;
import dna.graph.Node;
import dna.util.parameters.Parameter;
import dna.util.parameters.ParameterList;

public abstract class BatchGenerator<G extends Graph<N, E>, N extends Node<E>, E extends Edge>
		extends ParameterList {

	protected GraphDatastructures<G, N, E> datastructures;

	public BatchGenerator(String name, Parameter[] params,
			GraphDatastructures<G, N, E> datastructures) {
		super(name, params);
		this.datastructures = datastructures;
	}

	public abstract Batch<E> generate(Graph<? extends Node<E>, E> graph);

}