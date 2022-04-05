package ass1;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author George McLachlan
 *
 */
public class MParallelSorter2 implements Sorter{


	/**
	 * @param list to sort.
	 * @return The sorted list.
	 */
	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list){
		return mergeSort(list);
	}


	/**
	 * Gets recursively called to split up the original list into smaller pieces and delegates tasks to new threads if needed.
	 * 
	 * @param list to sort.
	 * @return Sorted list. In the last case, the final sorted list.
	 */
	private <T extends Comparable<? super T>> List<T> mergeSort(List<T> list){

		// efficient to just complete in this thread if list is this small
		if(list.size() < 20) return MSequentialSorter.mergeSort(list);

		int middle = list.size()/2;

		// split list in half
		// only allocate a new thread for one side so the current thread can keep working
		CompletableFuture<List<T>> firstHalfCompletableFuture = CompletableFuture.supplyAsync(() -> mergeSort(list.subList(0, middle)) );
		List<T> secondHalf = mergeSort(list.subList(middle, list.size()));

		return MSequentialSorter.merge(firstHalfCompletableFuture.join(), secondHalf);
	}

}
