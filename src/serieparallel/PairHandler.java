package serieparallel;

import java.util.ArrayList;

public class PairHandler { //A remplacer par une hashmap, les ID des noeuds sont uniques.
	private ArrayList<Pair<Node<?>, Node<?>>> pairs;
	
	public PairHandler() {
		pairs = new ArrayList<>();
	}
	
	public void add(Node<?> key, Node<?> value) {
		pairs.add(new Pair<Node<?>, Node<?>>(key, value));
	}
	
	public boolean containsKey(Node<?> node) {
		for(Pair<Node<?>, Node<?>> p : pairs)
			if(p.getKey().equals(node)) {
				return true;
			}
		return false;
	}
	
	public Node<?> get(Node<?> node) {
		for(Pair<Node<?>, Node<?>> p : pairs)
			if(p.getKey().equals(node)) {
				return p.getValue();
			}
		return null;
	}
	/*
	 * On veut faire un truc qui soit entre hashmap et arraylist... Surement pas le meilleur moyen.
	 */
	private class Pair<K, V> {
		private K n1;
		private V n2;
		
		private Pair(K a, V b) {
			n1 = a;
			n2 = b;
		}
		
		public K getKey() {
			return n1;
		}
		
		public V getValue() {
			return n2;
		}
		
		public final String toString() { return "(" + n1 + ";" + n2 + ")"; }

	}
	
}
