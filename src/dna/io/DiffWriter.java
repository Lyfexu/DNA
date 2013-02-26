package dna.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import dna.diff.Diff;
import dna.graph.Edge;

public class DiffWriter {
	public static boolean write(Diff d) {
		return DiffWriter.write(d, d.getFilename());
	}

	public static boolean write(Diff d, String filename) {
		return DiffWriter.write(d, filename, true);
	}

	public static boolean write(Diff d, String filename, boolean sortEdges) {
		Writer writer = null;
		try {
			writer = new Writer(filename);

			writer.writeKeyword(Keywords.diffNodes);
			writer.writeln(d.getNodes());

			writer.writeKeyword(Keywords.diffFrom);
			writer.writeln(d.getFrom());

			writer.writeKeyword(Keywords.diffTo);
			writer.writeln(d.getTo());

			writer.writeKeyword(Keywords.diffAddedEdges);
			writer.writeln(d.getAddedEdges().size());

			writer.writeKeyword(Keywords.diffRemovedEdges);
			writer.writeln(d.getRemovedEdges().size());

			writer.writeKeyword(Keywords.diffListOfAddedEdges);
			if (sortEdges) {
				ArrayList<Edge> sorted = new ArrayList<Edge>(new TreeSet<Edge>(
						d.getAddedEdges()));
				for (Edge e : sorted) {
					writer.writeln(e.getStringRepresentation());
				}
			} else {
				for (Edge e : d.getAddedEdges()) {
					writer.writeln(e.getStringRepresentation());
				}
			}

			writer.writeKeyword(Keywords.diffListofRemovedEdges);
			if (sortEdges) {
				ArrayList<Edge> sorted = new ArrayList<Edge>(new TreeSet<Edge>(
						d.getRemovedEdges()));
				for (Edge e : sorted) {
					writer.writeln(e.getStringRepresentation());
				}
			} else {
				for (Edge e : d.getRemovedEdges()) {
					writer.writeln(e.getStringRepresentation());
				}
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}