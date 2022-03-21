package ass1;

import java.util.ArrayList;
import java.util.List;

/**
 * @author George McLachlan
 *
 */
public class MSequentialSorter implements Sorter {


	/**
	 * The benefit of this algorithm is that it is sequential. When the list supplied is small enough
	 * it is faster to sequentially sort it rather then setting up threads and a thread pool because 
	 * there is overhead when using multiple threads. When used in conjunction with multithreaded tasks,
	 * having a sequential part is very powerful.
	 * <p>
	 * 
	 * Learned how to implement merge sort using generics. While doing this I learned how powerful generics
	 * are when used properly.
	 * <p>
	 * 
	 * @param list to sort.
	 * @return The sorted list.
	 */
	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list){
		return mergeSort(list);
	}


	/**
	 * Gets recursively called to split up the original list into smaller pieces.
	 * 
	 * @param list to sort. 
	 * @return The the sorted list. In the last case; The final sorted list.
	 */
	public static <T extends Comparable<? super T>> List<T> mergeSort(List<T> list){
		if(list.size() < 2) return list;

		int middle = list.size()/2;

		// split list in half
		List<T> firstHalf = mergeSort(list.subList(0, middle));
		List<T> secondHalf = mergeSort(list.subList(middle, list.size()));

		// merge and sort the halves together
		return merge(firstHalf, secondHalf);
	}


	/**
	 * Takes two lists and both merges and sorts them. Sorted by the objects comparator.
	 * 
	 * @param first list that extends Comparable
	 * @param second list that extends Comparable
	 * @return Merged and sorted list
	 */
	public static <T extends Comparable<? super T>> List<T> merge(List<T> first, List<T> second){
		ArrayList<T> merged = new ArrayList<T>();

		// initial indexes of first and second lists
		int firstIndex = 0;
		int secondIndex = 0;

		// compare each index of the subarrays adding the lowest value to the currentIndex
		while(firstIndex < first.size() && secondIndex < second.size()){
			if((first.get(firstIndex).compareTo(second.get(secondIndex))) <= 0){
				merged.add(first.get(firstIndex));
				firstIndex++;
			}else{
				merged.add(second.get(secondIndex));
				secondIndex++;
			}
		}

		// copy remaining elements of first list if any
		while(firstIndex < first.size()){
			merged.add(first.get(firstIndex));
			firstIndex++;
		}

		// copy remaining elements of second list if any
		while(secondIndex < second.size()){
			merged.add(second.get(secondIndex));
			secondIndex++;
		}

		return merged;
	}

}