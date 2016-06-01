package parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import serieparallel.Graph;
import serieparallel.Node;

public class TikzExporter {
	public static final int WITH_LABEL=0, NO_LABEL=1;
	static BufferedWriter writer;

	public static void export(File file, Graph g, int mode) {
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write("\\begin{tikzpicture}");
			for(Node<?> node : g.getSources())
				if(mode == WITH_LABEL) {
					exportWithLabel(node);
				}
			writer.write("\n\\end{tikzpicture}");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}

	private static void exportWithLabel(Node<?> node) {
		writeNodes(node);
		writeEdges(node);
	}
	
	private static void writeNodes(Node<?> node) {
		//\node (id) at (x,y) {label};
		StringBuilder sb = new StringBuilder("\n\\node (");
		sb.append(node.id).append(") at (");
		sb.append(node.x).append(",-").append(node.y);
		sb.append(") {").append(node.value).append("};");
		try {
			writer.write(sb.toString());
			writer.flush();
		} catch (IOException e) { e.printStackTrace(); }
		node.exportTag = 1;

		for(Node<?> child : node.getChildren())
			if(child.exportTag==0) {
				writeNodes(child);
			}
	}

	private static void writeEdges(Node<?> node) {
		node.exportTag = 0;
		for(Node<?> child : node.getChildren()) {
			// \draw[->] (id1) -- (id2);
			StringBuilder sb = new StringBuilder("\n\\draw[->] (");
			sb.append(node.id).append(") -- (");
			sb.append(child.id).append(");");
			try {
				writer.write(sb.toString());
				writer.flush();
			} catch (IOException e) { e.printStackTrace(); }
			if(child.exportTag==1) {
				writeEdges(child);
			}
		}
	}

}
