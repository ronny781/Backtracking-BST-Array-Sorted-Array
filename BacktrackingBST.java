

import java.util.NoSuchElementException;

public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
	private Stack stack;
	private Stack redoStack;
	private BacktrackingBST.Node root = null;
	private boolean IsOperationRegular= true;//

	// Do not change the constructor's signature
	public BacktrackingBST(Stack stack, Stack redoStack) {
		this.stack = stack;
		this.redoStack = redoStack;
	}

	public Node getRoot() {
		if (root == null) {
			throw new NoSuchElementException("empty tree has no root");
		}
		return root;
	}
	public boolean isEmpty() {
		return(root==null);
	}



	public Node search(int k) {//Return the node of the given key if exists
		// TODO: implement your code here	
		Node curr = root;
		while(curr!=null & k!=curr.key) {
			if(curr.key>k)
				curr = curr.left;
			else
				curr = curr.right;
		}
		return curr;
	}



	public void insert(Node node) {//Insert the following node to the tree.
		// TODO: implement your code here
		Node trail = null;
		Node curr = root;
		while(curr != null) {
			trail = curr;
			if(node.key < curr.key)
				curr = curr.left;
			else
				curr = curr.right;
		}
		node.parent = trail;
		if(trail == null)
			root = node;
		else if(node.key < trail.key)
			trail.left = node;
		else
			trail.right = node;

		if(IsOperationRegular) //In case that that this operation wan't initiated from Retracting then clear redoStack
			redoStack.clear();
		stack.push(new stackElements(true, node.key,node, -1));

	}


	public void delete(Node node) {//Delete the following node to the tree.

		//First case: node is a leaf
		if(node.left==null && node.right == null) {//Note: need to code unique case when the tree is the root only.
			Node parent = node.parent;
			if(parent.right== node) 
				parent.right = null;
			else
				parent.left = null;

			stack.push(new stackElements(false, node.key, node.parent, 1));
		}
		//Second case: node has one child, There are 4 sub-cases.
		else if(node.left==null || node.right == null){
			if (node.left == null) {
				node.right.parent = node.parent;
				if(node.key > node.parent.key) {//Case 21: the node has no left child and its key is bigger than his parent
					node.parent.right = node.right;
					stack.push(new stackElements(false, node.key, node.parent, 21));
				}

				else {//Case 22: the node has no left child and its key is smaller than his parent
					node.parent.left = node.right;
					stack.push(new stackElements(false, node.key, node.parent, 22));
				}
			}
			else {
				node.left.parent = node.parent;
				if(node.key > node.parent.key) {//Case 23: the node has no right child and its key is bigger than his parent
					node.parent.right = node.left;
					stack.push( new stackElements(false, node.key, node.parent, 23));
				}
				else {
					node.parent.left = node.left;//Case 23: the node has no right child and its key is smaller than his parent
					stack.push(new stackElements(false, node.key, node.parent, 24));
				}
			}
			if(IsOperationRegular) //In case that that this operation wan't initiated from Retracting then clear redoStack
				redoStack.clear();
		}
		//Third case: node has two child
		else { //Swap the successor and the intended for deletion node
			int prevKey = node.key;
			Node suc = successor(node);
			int sucKey = suc.key;
			delete(suc);
			node.key = sucKey;
			stack.push(new stackElements(false, prevKey, node, 3));
		}
	}


	public Node minimum() {//Return the minimum in this nested tree
		if(isEmpty())
			throw new NoSuchElementException("The tree is empty");
		BacktrackingBST.Node currNode = root;
		while (currNode.left != null) {
			currNode = currNode.left;
		}
		return currNode;
	}


	public Node maximum() {//Return the maximum in this nested tree
		if(isEmpty())
			throw new NoSuchElementException("The tree is empty");
		BacktrackingBST.Node currNode = root;
		while (currNode.right != null) {
			currNode = currNode.right;
		}
		return currNode;
	}


	public Node successor(Node node) {//The method return the successor for the given node
		if(node.right!=null) {
			Node temp = root;
			root = node.right;
			Node minimun = minimum();
			root = temp;
			return minimun;
		}

		Node parent = node.parent;
		while(parent!=null && parent.right==node) {
			node = parent;
			parent = parent.parent;
		}
		if(parent==null)	
			throw new NoSuchElementException("There is no successor");
		return parent;
	}


	public Node predecessor(Node node) {//The method return the predecessor for the given node
		if(node.left!=null) {
			Node temp = root;
			root = node.left;
			Node maximum = maximum();
			root = temp;
			return maximum;
		}
		Node parent = node.parent;
		while(parent!=null && parent.left==node) {
			node = parent;
			parent = parent.parent;
		}
		if(parent==null)	
			throw new NoSuchElementException("There is no predecessor");
		return parent;
	}

	@Override
	public void backtrack() {//The method stacking the last insert/delete operation
		if(stack.isEmpty())
			return;
		stackElements element = (BacktrackingBST.stackElements) stack.pop();

		if(element.Operator) {//First case - the last operation was that was stacked was an insertion 
			redoStack.push(new stackElements(true, element.pointer.key, element.pointer, -1));
			Node parent = element.pointer.parent; // delete the node manually
			if(parent == null)
				root =null;//delete the root
			else if(parent.right != null && parent.right.key == element.pointer.key) //need to check for yeilut
				parent.right = null;
			else  if(parent.left != null && parent.left.key == element.pointer.key) 
				parent.left = null;


		}
		else {//Second case - the last operation that was stacked - deletion 
			switch(element.caseNum) {
			//			
			case 1://The deleted element was a leaf
				if (element.pointer.key > element.key) {
					element.pointer.left = new Node(element.key, null);
					element.pointer.left.parent = element.pointer;
					redoStack.push(new stackElements(false, element.pointer.left.key, element.pointer.left, -1));
				}
				else {
					element.pointer.right = new Node(element.key, null);
					element.pointer.right.parent = element.pointer;
					redoStack.push(new stackElements(false, element.pointer.right.key, element.pointer.right, -1));
				}
				break;
			case 21://The deleted element had one child - There are 4 different cases
				Node toInsert1 = new Node(element.key,null);
				Node temp1 = element.pointer.right;
				element.pointer.right = toInsert1;
				toInsert1.parent = element.pointer;
				toInsert1.right = temp1;
				temp1.parent =  toInsert1;
				redoStack.push(new stackElements(false, toInsert1.key, toInsert1, -1));
				break;
			case 22:
				Node toInsert2 = new Node(element.key,null);
				Node temp2 = element.pointer.left;
				element.pointer.left = toInsert2;
				toInsert2.parent = element.pointer;
				toInsert2.right = temp2;
				temp2.parent =  toInsert2;
				redoStack.push(new stackElements(false, toInsert2.key, toInsert2, -1));
				break;
			case 23:
				Node toInsert3 = new Node(element.key,null);
				Node temp3 = element.pointer.right;
				element.pointer.right = toInsert3;
				toInsert3.parent = element.pointer;
				toInsert3.left = temp3;
				temp3.parent =  toInsert3;
				redoStack.push(new stackElements(false, toInsert3.key, toInsert3, -1));
				break;
			case 24:
				Node toInsert4 = new Node(element.key,null);
				Node temp4 = element.pointer.left;
				element.pointer.left = toInsert4;
				toInsert4.parent = element.pointer;
				toInsert4.left = temp4;
				temp4.parent = toInsert4;
				redoStack.push(new stackElements(false, toInsert4.key, toInsert4, -1));
				break;
			case 3:	//The deleted element had 2 children
				backtrack();//For reversing the deletion of the successor node
				element.pointer.key = element.key; // in this case the pointer refers to the deleted node
				redoStack.push(new stackElements(false, element.pointer.key , element.pointer, -1));


			}

		}
	}


	@Override
	public void retrack() { //The method stacking backtracking operations
		if(redoStack.isEmpty())
			return;

		stackElements element = (BacktrackingBST.stackElements) redoStack.pop();
		//In this case of we don't want to stack the operations in the backtrack stack.
		//So we made sure that the operation we'll be done without stacking in backtrack stack
		if(element.Operator) { 
			IsOperationRegular = false;
			insert(element.pointer);
			IsOperationRegular = true;
		}


		if(!element.Operator) {
			IsOperationRegular = false; 
			delete(element.pointer);
			IsOperationRegular = true;
		}

	}


	public void printPreOrder(){//Print Pre order iteratively
		if (isEmpty()) 
			return;

		Stack nodeStack = new Stack();
		nodeStack.push(root);

		while (!nodeStack.isEmpty()) {
			Node mynode = (BacktrackingBST.Node) nodeStack.pop();
			System.out.print(mynode.key + " ");
			if (mynode.right != null) 
				nodeStack.push(mynode.right);

			if (mynode.left != null) 
				nodeStack.push(mynode.left);

		}
	}

	@Override
	public void print() {
		printPreOrder();
	}
	public String printToString() {
		if(isEmpty())
			return " ";
		return root.toString();
	}


	public static class Node {
		// These fields are public for grading purposes. By coding conventions and best practice they should be private.
		public BacktrackingBST.Node left;
		public BacktrackingBST.Node right;

		private BacktrackingBST.Node parent;
		private int key;
		private Object value;

		public Node(int key, Object value) {
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}



	}
	static class stackElements{

		private boolean Operator; //True = Insert operation, False = Delete operation
		private int key; // node key
		private int caseNum;  //Only relevant to delete operation
		private Node pointer;//In case of delete pointer refers to parent 
		//In case  of insert the pointer refers to the inserted node	




		public stackElements(boolean operator, int key,Node pointer,int caseNum) {
			this.key = key;
			this.Operator = operator;
			this.pointer = pointer;
			this.caseNum = caseNum;

		}


	}



	public static void main (String[] args) {
		Stack st1 = new Stack();
		Stack st2 = new Stack();
		BacktrackingBST tree = new BacktrackingBST(st1,st2) ;
		BacktrackingBST.Node n1 = new BacktrackingBST.Node(12,null);
		BacktrackingBST.Node n2 = new BacktrackingBST.Node(6,null);
		BacktrackingBST.Node n3 = new BacktrackingBST.Node(29,null);
		BacktrackingBST.Node n4 = new BacktrackingBST.Node(10,null);
		BacktrackingBST.Node n5 = new BacktrackingBST.Node(13,null);
		BacktrackingBST.Node n6 = new BacktrackingBST.Node(-9,null);
		BacktrackingBST.Node n7 = new BacktrackingBST.Node(35,null);
		
		BacktrackingBST.Node n8 = new BacktrackingBST.Node(50,null);
		BacktrackingBST.Node n9 = new BacktrackingBST.Node(2,null);
		BacktrackingBST.Node n10 = new BacktrackingBST.Node(1,null);
		BacktrackingBST.Node n11 = new BacktrackingBST.Node(17,null);
		BacktrackingBST.Node n12 = new BacktrackingBST.Node(7,null);
		//	BacktrackingBST.Node n8 = new BacktrackingBST.Node(1,null);
		tree.insert(n1);
		tree.insert(n2);
		tree.insert(n3);

		System.out.println("//////////////////// - step 0");
		System.out.println(tree.printToString());
		System.out.println("//////////////////// - step 1");
		tree.insert(n4);
		System.out.println(tree.printToString());
		System.out.println("//////////////////// - step 2");
		tree.insert(n5);
		System.out.println(tree.printToString());
		System.out.println("//////////////////// - step 3");
		tree.insert(n6);
		System.out.println(tree.printToString());
		System.out.println("//////////////////// - step 4");
		tree.backtrack();
		System.out.println(tree.printToString());

		System.out.println("//////////////////// - step 5");
		System.out.println(tree.minimum().key);

		System.out.println("//////////////////// - step 6");
		tree.backtrack();
		System.out.println(tree.printToString());


		System.out.println("//////////////////// - step 7");
		tree.backtrack();
		System.out.println(tree.printToString());

		System.out.println("//////////////////// - step 8");
		tree.retrack();
		System.out.println(tree.printToString());

		System.out.println("//////////////////// - step 9");
		System.out.println(tree.maximum().key);

		System.out.println("//////////////////// - step 10");
		tree.retrack();
		System.out.println(tree.printToString());

		System.out.println("//////////////////// - step 11");
		tree.insert(n7);
		System.out.println(tree.printToString());

		System.out.println("//////////////////// - step 12");
		tree.retrack();
		System.out.println(tree.printToString());
		System.out.println("  sadsadsadsaed ");
		tree.insert(n8);
		tree.insert(n9);
		tree.insert(n10);
		tree.insert(n11);
		tree.insert(n12);
		System.out.println("before delete");System.out.println(tree.printToString());
		tree.delete(n4);
		System.out.println("after delete");System.out.println(tree.printToString());
		tree.backtrack();
		System.out.println("after backtrack");System.out.println(tree.printToString());
		tree.retrack();
		System.out.println("after retrak");System.out.println(tree.printToString());

		
		

	}

}
