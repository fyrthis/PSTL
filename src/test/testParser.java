package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import com.alexmerz.graphviz.ParseException;

import parser.mxDotParser;
import viewer.stepbystep.GraphComponent;
import viewer.stepbystep.mxGraph;
import viewer.stepbystep.mxThread;

public class testParser {

	@Test
	public void test() {
		mxDotParser parser;
		try {
			parser = new mxDotParser("samples/random-dag/g.50.1.graphml.dot");
			mxGraph graph = parser.getGraph();
			if(graph==null) System.out.println("graph is null");
			mxThread thread = new mxThread(graph);
			
		} catch( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Error e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCycle() {
		mxDotParser parser;
		try {
			parser = new mxDotParser("samples/random-dag/g.1.graphml.dot");
			mxGraph graph = parser.getGraph();
			if(graph==null) System.out.println("graph is null");
		} catch( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Error e){
			e.printStackTrace();
		}
	}

}
