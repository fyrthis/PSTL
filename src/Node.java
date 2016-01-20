import java.util.ArrayList;

public class Node {
	public int x;
	public int y;
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
	public ArrayList<Node> getChildren() {
		return children;
	}
	public ArrayList<Node> getParents() {
		return parents;
	}
	public void connect(Node node) {
		this.children.add(node);
		node.getParents().add(this);
	}
}
