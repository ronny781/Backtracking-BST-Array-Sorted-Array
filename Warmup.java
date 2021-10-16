
public class Warmup {
	public static int backtrackingSearch(int[] arr, int x, int forward, int back, Stack myStack) {
		int index = 0;
		while(index<arr.length) {
			for(int i = 0; i < forward & index<arr.length ; i++,index++) {
				if(arr[index]==x)
					return index;
				myStack.push(arr[index]);

			}
			for(int i = 0; i <back & index<arr.length; i++,index--) {
				myStack.pop();
				if(arr[index-1]==x)
					return index;
			}
		}
		return -1; // temporal return command to prevent compilation error 
	}
	public static void main(String[] args) {
		int [] arr = {1,1,2,7,15,16,23,99,100,100,100,132,193,196,197};
		Stack myStack = new Stack();
		System.out.println(consistentBinSearch(arr, 1, myStack));
		//		
		//		int [] arr = {1,7,7,7,7,8,4,9};
		//		Stack myStack = new Stack();
		//		System.out.println(backtrackingSearch(arr, 8 ,3 , 2, myStack));
	}	
	public static int consistentBinSearch(int[] arr, int x, Stack myStack) {
		// TODO: implement your code here
		int min = 0;
		int max = arr.length-1;
		while(max>=min) {
			int mid = (max + min)/2;
			if (arr[mid] == x)
				return mid; 	
			if(arr[mid]<x) {
				stackElements element = new stackElements(min,mid,max);
				myStack.push(element);
				min = mid +1;
			}
			else {
				stackElements element = new stackElements(min,mid,max);
				myStack.push(element);
				max = mid - 1;
			}
			for(int i = 0; i<Consistency.isConsistent(arr);i++) {
				if(!myStack.isEmpty()) {
					stackElements element = (stackElements) myStack.pop();
					min = element.min;
					max = element.max;
					mid = element.mid;
				}
			}

		}
		return -1;
	}
	static class stackElements{
		//This class is created for convenient storage of  min mid and max keys
		private int min;
		private int max;
		private int mid;

		public stackElements(int min, int mid, int max) {
			this.min = min;
			this.mid = mid;
			this.max = max;
		}


	}

}
