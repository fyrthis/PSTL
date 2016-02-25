package viewer.stepbystep;

import java.util.ArrayList;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

import serieparallel.Node;

public class mxNode extends mxCell {
	public Node n;
	private ArrayList<mxNode> children = new ArrayList<mxNode>();
	private ArrayList<mxNode> parents = new ArrayList<mxNode>();
	
	public mxNode(Node n)
	{
		super(n.value, null, null);
		this.n = n;
		setVisible(false);
	}
	
	public mxNode(Node n, Object value)
	{
		super(value, null, null);
		this.n = n;
		setVisible(false);
	}

	public mxNode(Node n, Object value, mxGeometry geometry, String style)
	{
		super(value, geometry, style);
		this.n = n;
		setVisible(false);
	}

	public mxNode(String id) {
		super(id, null, null);
		n = new Node(id, id);
		setVisible(false);
	}

	public void setX(double x) {
		geometry.setX(x*50);
		n.x = (int)x;
		
	}
	
	public void setY(double y) {
		geometry.setY(y*50);
		n.y = (int)y;
	}
	
	public double getX() {
		return geometry.getX();
	}
	
	public double getY() {
		return geometry.getY();
	}
	
	public ArrayList<mxNode> getParents() {
		return parents;
	}
	public ArrayList<mxNode> getChildren() {
		return children;
	}
	public void connect(mxNode node) {
		this.children.add(node);
		node.getParents().add(this);
	}
	public Object getValue() {
		return n.value;
	}
}
