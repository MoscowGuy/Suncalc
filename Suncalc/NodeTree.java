package Suncalc;

import java.util.ArrayList;

public class NodeTree {
	private String key;
	private City city;
	private NodeTree parent;
	private ArrayList<NodeTree> children;
	private boolean isRoot;
	private int keyStart;
	
	public NodeTree() {
		this.key = "";
		this.city = null;
		this.parent = null;
		this.children = null;
		this.isRoot = true;
		this.keyStart = 0;
	}
	
	public NodeTree(String key, int keyStart, NodeTree parent) {
		this.key = key;
		this.parent = parent;
		this.city = null;
		this.children = null;
		this.isRoot = false;
		this.keyStart = keyStart;
	}
	
	public NodeTree(String key, int keyStart, NodeTree parent, City city) {
		this.key = key;
		this.parent = parent;
		this.city = city;
		this.children = null;
		this.isRoot = false;
		this.keyStart = keyStart;
	}
	
	public NodeTree(String key, int keyStart, NodeTree parent, ArrayList<NodeTree> children) {
		this.key = key;
		this.parent = parent;
		this.children = children;
		this.city = null;
		this.isRoot = false;
		this.keyStart = keyStart;
	}
	
	public void removeCity() {
		this.city = null;
	}
	
	public NodeTree getChild(String prefix) {
		if(this.children != null) {
			for(NodeTree child : this.children) {
				String key = child.key.toLowerCase();
				String pr = prefix.toLowerCase();
				boolean isStarts;
				if(child.key.length() > prefix.length())
					isStarts = key.startsWith(pr);
				else
					isStarts = pr.startsWith(key);
				if(isStarts)
					return child;
			}
			return null;
		}
		else
			return null;
	}
	
	public ArrayList<NodeTree> getChildren() {
		return this.children;
	}
	
	public boolean isRoot() {
		return this.isRoot;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public int getKeyLength() {
		return this.key.length();
	}
	
	public int getKeyStartPosition() {
		return this.keyStart;
	}
	
	public boolean hasClidren() {
		if(this.children != null)
			return true;
		else
			return false;
	}
	
	public NodeTree getParent() {
		return this.parent;
	}
	
	public City getCity() {
		return this.city;
	}
	
	public void setChild(NodeTree node) {
		if(this.children == null)
			this.children = new ArrayList<NodeTree>();
		
		this.children.add(node);
	}
	
	public void setKey(String key) {
		this.key = key;
	}
}