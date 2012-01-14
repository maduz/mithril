package model;

public class Pair <K, V>{
	K left;
	V right;
	
	public Pair(K key, V value) {
		left = key;
		right = value;
	}
	
	public K getLeft() {return left; }
	public V getRight() { return right; }
}
