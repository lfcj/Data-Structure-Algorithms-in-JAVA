public class QuickSort {
	
	
	public static void quicksort(int[] a, int low, int high){
		if (low<high){
			int m = partition(a, low, high);
			quicksort(a, low, m-1);
			quicksort(a, m+1, high);
		}
	}
	
	public static int partition(int[] a, int low, int high){
		
		int pivot = a[low];
		int i = low;
		
		for (int j=low+1; j<= high; j++){
			if (a[j]<pivot){
				i = i+1;
				int eli = a[i];
				a[i] = a[j];
				a[j] = eli;
			}
		}
		int eli = a[i];
		a[i] = a[low];
		a[low] = eli;
		return i;
	}

}//END CLASS
