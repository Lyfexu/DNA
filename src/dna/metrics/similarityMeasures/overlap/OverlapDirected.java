package dna.metrics.similarityMeasures.overlap;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import dna.graph.IElement;
import dna.graph.nodes.DirectedNode;
import dna.graph.nodes.Node;
import dna.metrics.Metric;
import dna.metrics.similarityMeasures.Matrix;
import dna.metrics.similarityMeasures.MeasuresDirectedUnweighted;
import dna.series.data.BinnedDistributionLong;
import dna.series.data.Distribution;
import dna.util.parameters.Parameter;

/**
 * Computes the overlap similarity measure for graphs with directed and
 * unweighted edges. The overlap similarity of two nodes <i>n</i>, <i>m</i> is
 * defined as the number of elements in the intersection of <i>neighbors(n)</i>
 * and <i>neighbors(m)</i> divided by
 * min(|<i>neighbors(n)</i>|,|<i>neighbors(m)</i>|).
 * 
 * @see OverlapDirectedR
 * @see OverlapDirectedU
 */
public abstract class OverlapDirected extends MeasuresDirectedUnweighted {
	/** Contains the number of neighbors for each node */
	protected HashMap<DirectedNode, Integer> amountOfNeighbors;

	public OverlapDirected(String name, ApplicationType applicationType) {
		super(name, applicationType);
	}

	public OverlapDirected(String name, ApplicationType type,
			Parameter directedDegreeType) {
		super(name, type, directedDegreeType);
	}

	@Override
	public boolean compute() {
		final Collection<IElement> nodesOfGraph = this.g.getNodes();

		DirectedNode node1, node2;
		// neighbors for node1, node2:
		HashSet<DirectedNode> neighbors1, neighbors2;
		// indices for both for-loops to save some time with using matching(1,2)
		// = matching(2,1)
		int nodeIndex1 = 0, nodeIndex2;

		for (IElement iElement1 : nodesOfGraph) {
			node1 = (DirectedNode) iElement1;
			neighbors1 = this.getNeighborNodes(node1);
			this.amountOfNeighbors.put(node1, neighbors1.size());
			nodeIndex2 = 0;
			for (IElement iElement2 : nodesOfGraph) {
				if (nodeIndex2 < nodeIndex1) {
					// matching is equal to equivalent calculated before
					// (matching(1,2) = matching(2,1))
					nodeIndex2++;
					continue;
				}

				node2 = (DirectedNode) iElement2;
				neighbors2 = this.getNeighborNodes(node2);

				HashSet<DirectedNode> intersection = getMatching(neighbors1,
						neighbors2);

				this.matching.put(node1, node2, (double) intersection.size());

				double numerator = intersection.size();
				double denominator = Math.min(neighbors1.size(),
						neighbors2.size());
				double fraction;
				if (numerator == 0 || denominator == 0)
					fraction = 0;
				else
					fraction = numerator / denominator;

				this.result.put(node1, node2, fraction);
				this.binnedDistribution.incr(fraction);

				nodeIndex2++;
			}

			nodeIndex1++;
		}

		return true;
	}

	@Override
	public boolean equals(Metric m) {
		if (m != null && m instanceof OverlapDirected) {
			// System.out.println(this.overlapSimilarity.toString());
			// System.out.println(((OverlapDirected) m).overlapSimilarity
			// .toString());
			return ((OverlapDirected) m).result.equals(this.result, 1.0E-4);
		}
		return false;
	}

	@Override
	public Distribution[] getDistributions() {
		this.binnedDistributionEveryNodeToOtherNodes = new BinnedDistributionLong(
				"BinnedDistributionEveryNodeToOtherNodes", 0.01, new long[] {},
				0);

		for (IElement iterable_element : this.g.getNodes()) {

			double index = this.result.getRowSum((Node) iterable_element)
					/ this.g.getNodeCount();
			this.binnedDistributionEveryNodeToOtherNodes.incr(index);
		}
		this.binnedDistribution.truncate();
		this.binnedDistributionEveryNodeToOtherNodes.truncate();

		return new Distribution[] { this.binnedDistribution,
				this.binnedDistributionEveryNodeToOtherNodes };
	}

	@Override
	public void init_() {
		this.result = new Matrix();
		this.amountOfNeighbors = new HashMap<DirectedNode, Integer>();
		this.matching = new Matrix();
		this.binnedDistribution = new BinnedDistributionLong(
				"OverlapDirectedD", 0.1, new long[] {}, 0);
		this.binnedDistributionEveryNodeToOtherNodes = new BinnedDistributionLong(
				"BinnedDistributionEveryNodeToOtherNodes", 0.01, new long[] {},
				0);
	}

	@Override
	public boolean isComparableTo(Metric m) {
		return m != null
				&& m instanceof OverlapDirected
				&& (((OverlapDirected) m).isOutgoingMeasure() == this
						.isOutgoingMeasure());
	}

	@Override
	public void reset_() {
		this.result = new Matrix();
		this.amountOfNeighbors = new HashMap<DirectedNode, Integer>();
		this.matching = new Matrix();
		this.binnedDistribution = new BinnedDistributionLong(
				"OverlapDirectedD", 0.1, new long[] {}, 0);
		this.binnedDistributionEveryNodeToOtherNodes = new BinnedDistributionLong(
				"BinnedDistributionEveryNodeToOtherNodes", 0.01, new long[] {},
				0);
	}
}