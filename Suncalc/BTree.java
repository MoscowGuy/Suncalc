package Suncalc;

public class BTree {
	private NodeTree root;
	private NodeTree cursor;
	
	public BTree() {
		this.root = new NodeTree();
		this.cursor = null;
	}
	
	public BTree(NodeTree root) {
		this.root = root;
		this.cursor = null;
	}
	
	private NodeTree search(String key) {
		if(this.root != null) {
			this.cursor = root;
			while(this.cursor.hasClidren() && key != "") {
				NodeTree current = cursor.getChild(key);
				if(current != null) {
					this.cursor = current;
				}
				else
					return null;
				if(cursor.getKeyLength() < key.length())
					key = key.substring(cursor.getKeyLength(), key.length());
				else
					key = "";
			}
		}
		return this.cursor;
	}
	
	private int checkContains(NodeTree node, String prefix) {
		int i = 0;
		char[] prefC = prefix.toCharArray();
		for(NodeTree child : node.getChildren()) {
			String key = child.getKey();
			char[] keyC = key.toCharArray();
			int length = key.length();
			if(prefix.length() < length)
				length = prefix.length();
			
			while(i < length) {
				if(prefC[i] == keyC[i])
					i++;
				else
					break;
			}
			
			if(i > 0) {
				this.cursor = child;
				return i;
			}			
		}
		return i;
	}
	
	private void add(String key, City city) {
		search(key);
		int keyStartPosition = cursor.getKeyStartPosition() + cursor.getKeyLength();
		if(keyStartPosition > 0)
			key = key.substring(keyStartPosition, key.length());
		if(this.cursor.hasClidren()) {
			int i = checkContains(this.cursor, key);
			if(i == 0) {
				NodeTree newNode = new NodeTree(key, keyStartPosition, this.cursor, city);
				this.cursor.setChild(newNode);
			}
			else {
				NodeTree oldNode = new NodeTree(this.cursor.getKey().substring(i), keyStartPosition + i,
						this.cursor, this.cursor.getCity());
				this.cursor.removeCity();
				NodeTree newNode = new NodeTree(key.substring(i), keyStartPosition + i,
						this.cursor, city);
				this.cursor.setChild(oldNode);
				this.cursor.setChild(newNode);
				this.cursor.setKey(this.cursor.getKey().substring(0, i));
			}
		}
		else
		{
			NodeTree newNode;
			if(this.cursor == this.root) {
				newNode = new NodeTree(key, 0, this.root, city);
			} else {
				newNode = new NodeTree(key, keyStartPosition, this.cursor, city);
				NodeTree oldNode = new NodeTree("", keyStartPosition, this.cursor, this.cursor.getCity());
				this.cursor.setChild(oldNode);
				this.cursor.removeCity();
			}
			this.cursor.setChild(newNode);
		}
	}
	
	public void printTreeInConsole() {
		this.cursor = this.root;
		System.out.println("*");
		int tab = 2;
		printChildren(this.cursor, tab);
	}
	
	private void printChildren(NodeTree node, int tab) {
		if(node.hasClidren()) {
			for(NodeTree child : node.getChildren()) {
				for(int i = 0; i < tab; i++)
					System.out.print("-");
				System.out.println(child.getKey());
				printChildren(child, tab+2);
			}
		}
	}
	
	public NodeTree getNode(String key) {
		NodeTree node = search(key);
		return node;
	}
	
	public void setRoot(NodeTree root) {
		this.root = root;
	}
	
	public void setCity(City city) {
		String key = city.getName() + ", " + city.getCountry().getName();
		add(key, city);
	}
}