public class HeapSort {

	public int getLeft(int parentIndex) {
		return (2 * parentIndex) + 1;
	}

	public int getRight(int parentIndex) {
		return (2 * parentIndex) + 2;
	}

	public void heap(int[] array, int parentIndex) {
		int size = array.length;
		int left = getLeft(parentIndex);
		int right = getRight(parentIndex);
		int smallest = parentIndex;
		if (size > left && array[left] < array[parentIndex]) {
			smallest = left;
		}

		if (size > right && array[right] < array[smallest]) {
			smallest = right;
		}
		if (smallest != parentIndex) {
			swap(array, parentIndex, smallest);
			heap(array, smallest);
		}
	}
	
	public void buildHeap(int [] array){
		 int size = array.length;
		 for (int i = (int)Math.floor(size/2);i>=0;i--){
			 heap(array,i);
		 }
	}

	static void swap(int[] array, int index1, int index2) {
		int temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}
	static String printArray(int[] array) {
		String res = "[";
		if (array.length != 0) {
			for (int i = 0; i < array.length - 1; i++) {
				res += array[i];
				res += " ,";
			}
			res += array[array.length - 1];
			res += "]";
			return res;
		} else
			return "";
	}

	public static void main(String[] args) {
		HeapSort sort = new HeapSort();
		int[] array = new int[] { 3,4,4,5,6,4,4,4,4,4,4,1 };
		sort.buildHeap(array);
		System.out.println(printArray(array));
	}
}
