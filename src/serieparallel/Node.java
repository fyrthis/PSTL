package serieparallel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Node<T> extends Point.Float {
	private static final long serialVersionUID = -324274001558021403L;
	
	public final String id;
	public T value;
	private List<Node<?>> parents;
	private List<Node<?>> children;
	//Coordinates x,y inherited
	public float offset;
	
	//Détection cycle
	public int detectionCycle;
	public int color=0;
	
	//Tags utilisés pour l'algorithme, et l'affichage.
	public boolean visited = false;
	public int printTag = 0;
	public int findDepthTag=0;
	public int initializationTag=0;
	public int applyOffsetsTag=0;
	public int findDiadmondsTag=0;
	public int exportTag=0;

	public int spaceTag=0;

	
	/**
	 * 
	 * @param id : to identify the node
	 * @param value : value of the node
	 */
	public Node(String id, T value) {
		this.id=id;
		this.value=value;
		x=0;
		y=0;
		offset=0;
		parents = new ArrayList<>();
		children = new ArrayList<>();
	}
	

	/**
	 * Connect two nodes between them.
	 * A node cannot be father twice of the same node.
	 * @param node : node that will become child of this
	 */
	public void connect(Node<?> node) {
		this.children.add(node);
		node.getParents().add(this);
	}
	
	/**
	 * 
	 * @return Every ancestors of this including source(s)
	 */
	public HashSet<Node<?>> getAncestors() {
		HashSet<Node<?>> descendants =  new HashSet<>();
		for(Node<?> parent : parents) {
			descendants.add(parent);
			descendants.addAll(parent.getAncestors());
		}
		return descendants;
	}
	
	/**
	 * 
	 * @return Every descendants including sink(s)
	 */
	public HashSet<Node<?>> getDescendants() {
		HashSet<Node<?>> descendants =  new HashSet<>();
		for(Node<?> child : children) {
			descendants.add(child);
			descendants.addAll(child.getDescendants());
		}
		return descendants;
	}
	
	/**
	 * see connect
	 * @param node
	 */
	public void addParent(Node<?> node) {
		node.connect(this);
	}
	
	/**
	 * see connect
	 * @param node
	 */
	public void addChild(Node<?> node) {
		this.connect(node);
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public boolean isSource() {
		return parents.isEmpty();
	}
	
	public boolean isSink() {
		return children.isEmpty();
	}
	
	public String toString() {
		return this.id;
	}
	
	public String valueToString(int length) {
		if(length<0 || length>=value.toString().length())
			return value.toString();
		return value.toString().substring(0, length);
	}

	public String getId() {
		return id;
	}

	public List<Node<?>> getParents() {
		return parents;
	}
	public List<Node<?>> getChildren() {
		return children;
	}


	public int getDetectionCycle() {
		return detectionCycle;
	}
	
	public void setDetectionCycle(int i){
		this.detectionCycle = i;
	}

	public int getColor() {
 		return color;
 	}
 	
 	public void setColor(int i){
 		this.color = i;
 	}
	
}
