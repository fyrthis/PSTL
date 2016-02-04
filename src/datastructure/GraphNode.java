package datastructure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;



public class GraphNode implements Cloneable, Serializable{

	private static final long serialVersionUID = 1L;

	public int x;
	public int y;
	public String label;
	private ArrayList<GraphNode> children = new ArrayList<GraphNode>();
	private ArrayList<GraphNode> parents = new ArrayList<GraphNode>();

	public int offset;
	
	public GraphNode(String label) {
		this.label=label;
		x=0;
		y=0;
		offset=0;
	}
	public GraphNode(int x, int y){
		this.x=x;
		this.y=y;
	}
	public GraphNode(String label, int x, int y){
		this.x=x;
		this.y=y;
		this.label=label;
	}

	public ArrayList<GraphNode> getParents() {
		return parents;
	}
	public ArrayList<GraphNode> getChildren() {
		return children;
	}
	
	public void connect(GraphNode node) {
		this.children.add(node);
		node.getParents().add(this);
	}
	
	public HashSet<GraphNode> getDescendants() {
		HashSet<GraphNode> descendants =  new HashSet<>();
		for(GraphNode child : children) {
			descendants.add(child);
			descendants.addAll(child.getDescendants());
		}
		return descendants;
	}
	
	public String toString() {
		if(label!=null)
			return label;
		else 
			return new String(label+"= ["+x+";"+y+"]");
	}
	public String getLabel() {
		return label;
	}


}
