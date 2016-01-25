package datastructure;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Node implements Cloneable {
	public int x;
	public int y;
	ArrayList<Node> parents;
	ArrayList<Node> children;
	public String label;
	
	public Node(String label) {
		this.label = label;
		x=0;
		y=0;
		parents = new ArrayList<Node>();
		children = new ArrayList<Node>();
	}
	public Node(int x, int y){
		this.x = x;
		this.y = y;
		parents = new ArrayList<Node>();
		children = new ArrayList<Node>();
	}
	public Node(String label, int x, int y){
		this.x = x;
		this.y = y;
		this.label = label;
		parents = new ArrayList<Node>();
		children = new ArrayList<Node>();
	}
	
	//Package visibility, SerieGraphParallel should be the only one to get access to nodes.
	//The viewer too.
	ArrayList<Node> getChildren() { 
		return children;
	}
	ArrayList<Node> getParents() {
		return parents;
	}
	
	public void connect(Node node) {
		this.children.add(node);
		node.getParents().add(this);
	}
	
	public String toString() {
		return new String(label+"= ["+x+";"+y+"]");
	}
	public String getLabel() {
		return label;
	}
	@Override
	public Node clone() throws CloneNotSupportedException {
		//Copie profonde très difficile, on risque de dupliquer 
		//  les fils et les pères...
		return (Node) super.clone();
		
		
	}
	public HashSet<Node> getDescendants() {
		HashSet<Node> descendants =  new HashSet<>();
		for(Node child : children) {
			descendants.add(child);
			descendants.addAll(child.getDescendants());
		}
		return descendants;
	}
}
