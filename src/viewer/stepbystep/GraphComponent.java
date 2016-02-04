package viewer.stepbystep;

import com.mxgraph.swing.mxGraphComponent;

public class GraphComponent extends mxGraphComponent{
	private static final long serialVersionUID = 1L;
	private mxThread thread;
	
	
	public GraphComponent(mxThread thread) {
		super(thread.getGraph());
		this.thread=thread;
		thread.start();
	}

	public void interrupt() {
		this.thread.interrupt();
	}

	public void event(boolean b) {
		if(thread!=null)
			thread.event(b);
		else
			System.out.println("thread is null");
	}
}
