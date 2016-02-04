package controler;

import java.util.Random;

import datastructure.TreeNode;
import datastructure.graph.Tree;

public class TreeControler {
	
	public TreeControler() {
		
	}
	Random r;
	public Tree randomTree(int depth, int large) {
		Tree tree = new Tree();
		r = new Random(15);
		TreeNode source = new TreeNode("source");
		tree.setSource(source);
		randomNodes(source, depth, large);
		return tree;
		
	}

	private void randomNodes(TreeNode father, int depth, int large) {
		if(depth>0) {
			int largeur = r.nextInt(large+1);
			for(int j = 0; j < largeur; j++) {
				TreeNode node = new TreeNode((j+1)+":"+largeur);
				father.connect(node);
				randomNodes(node,depth-1, large);
			}
		}
	}
}
