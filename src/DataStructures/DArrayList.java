package DataStructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import Graph.IElement;
import Graph.Edges.Edge;
import Graph.Nodes.Node;
import Utils.Rand;

public class DArrayList extends DataStructure implements INodeListDatastructure, IEdgeListDatastructure {
	private ArrayList<IElement> list;
	private int maxNodeIndex;

	public DArrayList(Class<? extends IElement> dT) {
		this.init(dT, defaultSize);
	}
	
	public void init(Class<? extends IElement> dT, int initialSize) {
		this.dataType = dT;
		this.list = new ArrayList<>(initialSize);
		this.maxNodeIndex = -1;
	}
	
	public boolean add(IElement element) {
		if (element instanceof Node) return this.add((Node) element);
		if (element instanceof Edge) return this.add((Edge) element);
		throw new RuntimeException("Can't handle element of type " + element.getClass() + " here");
	}	
	
	public boolean add(Node element) {
		super.canAdd(element);
		if (this.list.contains(element) || !this.list.add(element)) {
			return false;
		}
		this.maxNodeIndex = Math.max(this.maxNodeIndex,element.getIndex());
		return true;
	}
	
	public boolean add(Edge element) {
		super.canAdd(element);
		return !this.list.contains(element) && this.list.add(element);
	}	

	@Override
	public boolean contains(IElement element) {
		return list.contains(element);
	}

	@Override
	public int size() {
		return list.size();
	}
	
	public Node get(int index) {
		IElement n = null;

		// check node at $index
		if (this.list.size() > index) {
			n = this.list.get(index);
			if (n != null && n.getIndex() == index) {
				return (Node) n;
			}
		}

		// check nodes before $index
		if (n == null || n.getIndex() > index) {
			for (int i = Math.min(index - 1, this.list.size() - 1); i >= 0; i--) {
				IElement n2 = this.list.get(i);
				if (n2 != null && n2.getIndex() == index) {
					return (Node) n2;
				}
			}
		}

		// check nodes after $index
		if (n == null || n.getIndex() < index) {
			for (int i = index + 1; i < this.list.size(); i++) {
				IElement n2 = this.list.get(i);
				if (n2 != null && n2.getIndex() == index) {
					return (Node) n2;
				}
			}
		}

		return null;
	}
	
	public Edge get(Edge e) {
		for (IElement edge : this.list) {
			if (edge.equals(e)) {
				return (Edge) edge;
			}
		}
		return null;
	}	

	@Override
	public int getMaxNodeIndex() {
		return this.maxNodeIndex;
	}

	@Override
	public boolean remove(IElement element) {
		if ( element instanceof Node ) return this.remove((Node) element);
		if ( element instanceof Edge ) return this.remove((Edge) element);
		else throw new RuntimeException("Cannot remove a non-node from a node list");
	}

	@Override
	public boolean remove(Node element) {
		if (!this.list.remove(element)) {
			return false;
		}
		if (this.maxNodeIndex == element.getIndex()) {
			int max = -1;
			for (IElement n : this.getElements()) {
				max = Math.max(n.getIndex(), max);
			}
			this.maxNodeIndex = max;
		}
		return true;
	}
	
	public boolean remove(Edge e) {
		return this.list.remove(e);
	}

	@Override
	public IElement getRandom() {
		return this.list.get(Rand.rand.nextInt(this.list.size()));
	}

	@Override
	public Collection<IElement> getElements() {
		return this.list;
	}

	@Override
	public Iterator<IElement> iterator() {
		return this.list.iterator();
	}
}
