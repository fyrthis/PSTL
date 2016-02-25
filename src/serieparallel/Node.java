package serieparallel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Node<T> extends Point {
	private static final long serialVersionUID = -324274001558021403L;
	
	public final String id;
	public T value;
	//Coordinates x,y inherited
	public int offset;
	private List<Node<?>> parents;
	private List<Node<?>> children;
	
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
		if(!children.contains(node))
			this.children.add(node);
		if(!parents.contains(node))
			node.parents.add(this);
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
		StringBuilder sb = new StringBuilder("Node[").append(x).append(',').append(y).append("] : ID=");
		sb.append(id).append("\tValue=");
		if(value==null) sb.append("null");
		else {
			if(value.toString().length() > 20)
				sb.append(value.toString().subSequence(0, 20)).append("...");
			else
				sb.append(value.toString());
		}
		return sb.toString();
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
	
}
