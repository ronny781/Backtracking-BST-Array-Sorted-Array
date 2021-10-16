import java.util.NoSuchElementException;

public class BacktrackingArray implements Array<Integer>, Backtrack {
	private Stack stack;
	private int[] arr;
	private int lastIndex = 0;
	// TODO: implement your code here

	// Do not change the constructor's signature
	public BacktrackingArray(Stack stack, int size) {
		this.stack = stack;
		arr = new int[size];
	}

	@Override
	public Integer get(int index){
		// TODO: implement your code here
		return arr[index]; 
	}

	@Override
	public Integer search(int k) {//The method searches for the value k in the array,and returns its index if exist
		// TODO: implement your code here
		for(int i = 0; i < lastIndex; i++) {
			if(arr[i] == k)
				return i;
		}	
		return -1;
	}

	@Override
	public void insert(Integer x) {//Insert x to the array if there is space
		// TODO: implement your code here
		if(lastIndex == arr.length)
			throw new IllegalStateException("Array is full");
		boolean isFound = false;
		for(int i = 0; i < lastIndex &!isFound; i++) {
			if(arr[i] == x) {
				isFound = true;
				stack.push(new stackElements(true,-1,-1));
			}

		}
		if(!isFound) {
			arr[lastIndex] = x;
			stack.push(new stackElements(true,lastIndex,x));
			lastIndex++;
		}

	}

	@Override
	public void delete(Integer index) {//Delete x from the array if exists
		// TODO: implement your code here
		if(index < 0 || index >= lastIndex) {
			stack.push(new stackElements(false,-1,-1));
			return;
		}
		int number = arr[index];
		for(int i = index; i < lastIndex; i++) {
			arr[i] = arr[i+1];
		}
		arr[lastIndex] = 0;
		stack.push(new stackElements(false,index,number));
		lastIndex--;
	}

	@Override
	public Integer minimum() { //Return the index of the key with the minimal value in the array
		// TODO: implement your code here
		if(lastIndex==0)
			throw new NoSuchElementException("The array is empty");
		int minIndex = 0;
		for(int i=1; i < lastIndex; i++) {
			if(arr[i] < arr[minIndex])
				minIndex = i;
		}
		return minIndex; 
	}

	@Override
	public Integer maximum() {//Return the index of the key with the maximal value in the array
		// TODO: implement your code here
		if(lastIndex==0)
			throw new NoSuchElementException("The array is empty");
		int maxIndex = 0;
		for(int i=1; i < lastIndex; i++) {
			if(arr[i] > maxIndex)
				maxIndex = i;
		}
		return maxIndex;  
	}

	@Override
	public Integer successor(Integer index) {//Return the index of the successor for the given index's value
		// TODO: implement your code here 
		boolean isFound = false;
		int successor = index;
		for(int i = 0; i < lastIndex ; i++) {
			if(arr[i] > arr[successor] & !isFound) {
				successor = i;
				isFound = true;
			}
			if(arr[i] > arr[index] & isFound & arr[i] < arr[successor])
				successor = i;
		}
		if(isFound)
			return successor; 
		throw new NoSuchElementException("No successor found");
	}

	@Override
	public Integer predecessor(Integer index) {//Return the index of the predecessor for the given index's value
		// TODO: implement your code here
		boolean isFound = false;
		int predecessor = index;
		for(int i = 0; i < lastIndex ; i++) {
			if(arr[i] < arr[predecessor] & !isFound) {
				predecessor = i;
				isFound = true;
			}
			if(arr[i] < arr[index] & isFound & arr[i] > arr[predecessor])
				predecessor = i;
		}
		if(isFound)
			return predecessor; 
		throw new NoSuchElementException("No predecessor found");
	}

	@Override
	public void backtrack() { //Undo last action
		// TODO: implement your code here
		stackElements element = (BacktrackingArray.stackElements) stack.pop();
		if(element.index == -1)
			return;
		if(element.Operator)
			delete(lastIndex-1);
		// last case: backtrack the deleted number
		else {
			for(int i = lastIndex-1; i >= element.index ; i--) 
				arr[i+1] = arr[i];
			arr[element.index] = element.value;
			lastIndex++;

		}
	}
	@Override
	public void retrack() {
		/////////////////////////////////////
		// Do not implement anything here! //
		/////////////////////////////////////
	}

	@Override
	public void print() {
		// TODO: implement your code here
		for(int i = 0; i < lastIndex ; i++) {
			System.out.print(arr[i] + " ");
		}
	}

	class stackElements{
		//True = Insert, False = Delete
		private boolean Operator;
		private int index;
		private int value;

		public stackElements(boolean operator, int index, int value) {
			this.index = index;
			this.value = value;
			this.Operator = operator;
		}

	}
	public static void main (String [] args) {
		int [] arr = {1,3,5,7,9,13,16,19,20,22,55};
		Stack st = new Stack();
		BacktrackingArray array = new BacktrackingArray(st,20);
		array.insert(1);
		array.insert(3);
		array.insert(4);
		array.insert(7);
		array.insert(8);
		array.insert(5);
		array.print();
		System.out.println();
		array.delete(4);
		array.print();
		array.backtrack();
		System.out.println();
		array.print();
		
//		System.out.println();
//		System.out.println(array.minimum());
//		System.out.println(array.maximum());
//		System.out.println(array.predecessor(4));
//		
		
	}

}
