package xserieparallel;

import com.mxgraph.model.mxCell;

import serieparallel.Node;

public class XNode<T> extends Node<T> {
	private static final long serialVersionUID = -6077156099133318314L;
	public mxCell cell;

	public XNode(String id, T value) {
		super(id, value);
		cell = new mxCell(value);
	}
	
	public void setX(int x) {
		this.x=x;
		cell.getGeometry().setX((double)x);
	}
	
	public void setY(int y) {
		this.y=y;
		cell.getGeometry().setY((double)y);
	}
	
	
	
	
	

}
