import java.util.NoSuchElementException;


public class BacktrackingSortedArray implements Array<Integer>, Backtrack {
	private Stack stack;
	public int[] arr; 
	private int lastIndex = 0;
	// This field is public for grading purposes. By coding conventions and best practice it should be private.
	// TODO: implement your code here

	// Do not change the constructor's signature
	public BacktrackingSortedArray(Stack stack, int size) {
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
		int min = 0;
		int max = lastIndex-1;
		while (min <= max) {
			int mid = (min + max)/2;
			if (arr[mid] == k)
				return mid;
			if (arr[mid] < k)
				min = mid + 1;
			else
				max = mid - 1;
		}	
		return -1; 
	}

	@Override
	public void insert(Integer x) {//Insert x to the array if there is space
//		// TODO: implement your code here
		if(lastIndex == arr.length)
			throw new IllegalStateException("Array is full");
		if(lastIndex == 0) {
			arr[0] = x;
			lastIndex++;
			stack.push(new stackElements(true, -1, -1));
			return;
		}
		int min = 0;
		int max = lastIndex - 1;
		int mid = 0;
		while (min <= max) {
			mid = (min+max)/2;
			if (arr[mid] == x) {
				stack.push(new stackElements(true, -1, -1));
				return;		
			}
			if(arr[mid] < x)
				min = mid + 1;
			else 
				max = mid - 1;	
		}
		if(x<arr[mid]) 
			mid = mid-1;
		for(int i = lastIndex; i > mid + 1; i--) 
			arr[i] = arr[i-1];
		arr[mid+1] = x;
		stack.push(new stackElements(true, mid+1, x));
		lastIndex++;
	}

	@Override
	public void delete(Integer index) {//Delete x from the array if exists
		// TODO: implement your code here
		int number = arr[index];
		if(index<0 || index > lastIndex) {
			stack.push(new stackElements(false, -1, -1));
			return;
		}
		for(int i = index; i < lastIndex; i++) 
			arr[i] = arr[i+1];
		arr[lastIndex] = 0;
		lastIndex--;
		stack.push(new stackElements(false, index, number));
		
	}

	@Override
	public Integer minimum() {//Return the index of the key with the minimal value in the array
		// TODO: implement your code here
		if(lastIndex==0)
			throw new NoSuchElementException("The array is empty");
		return 0; 
	}

	@Override
	public Integer maximum() {//Return the index of the key with the maximal value in the array				
		// TODO: implement your code here
		if(lastIndex==0)
			throw new NoSuchElementException("The array is empty");
		return lastIndex-1; 
	}

	@Override
	public Integer successor(Integer index) {//Return the index of the successor for the given index's value
		// TODO: implement your code here
		if(index<0 || index >= lastIndex) 
			throw new IllegalArgumentException("Index out of range");

		if(index == lastIndex-1)
			throw new NoSuchElementException("There is no successor");
		return index+1; 
	}

	@Override
	public Integer predecessor(Integer index) {//Return the index of the predecessor for the given index's value
		// TODO: implement your code here
		if(index<0 || index >= lastIndex) 
			throw new IllegalArgumentException("Index out of range");

		if(index == 0)
			throw new NoSuchElementException("There is no successor");
		return index-1;  
	}


	@Override
	public void backtrack() {
		// TODO: implement your code here
		stackElements element = (BacktrackingSortedArray.stackElements) stack.pop();
		if(element.index == -1)
			return;
		if(element.Operator)
			delete(element.index);
		// last case: backtrack the deleted number
		else {
			insert(element.value);

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
		System.out.println();
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
		
		Stack st = new Stack();
		BacktrackingSortedArray array = new BacktrackingSortedArray(st,20);
		array.insert(3);
		array.insert(4);
		array.insert(7);
		array.insert(21);
		array.insert(-3);
		array.insert(4);
		array.insert(7);
		array.insert(21);
		array.print();
		array.delete(4);
		array.print();
		array.backtrack();
		array.print();
		
		
	}

}
