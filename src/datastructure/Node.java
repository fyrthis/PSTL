package datastructure;
import java.util.ArrayList;

public class Node {
	public int x;
	public int y;
	public int depth; //i.e. nb d'ancetres jusqu'à la racine + 1
	public int nbDesc; //i.e. nb de descendants jusqu'au puit
	//En connaissant ça, on peut avoir son ordonnée.
	ArrayList<Node> parents;
	ArrayList<Node> children;
	public String label;
	
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
}
