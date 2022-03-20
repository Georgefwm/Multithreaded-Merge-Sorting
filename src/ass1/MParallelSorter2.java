package ass1;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author George McLachlan
 *
 */
public class MParallelSorter2 implements Sorter {

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list){
		return MergeSort(list);
	}


	/**
	 * Gets recursively called to split up the original list into smaller pieces.
	 * 
	 * @param List<T> to sort. 
	 * 
	 * @return Sorted list. In the last case, the final sorted list.
	 */
	private <T extends Comparable<? super T>> List<T> MergeSort(List<T> list){
		if(list.size() < 20) return MSequentialSorter.mergeSort(list);

		int middle = list.size()/2;

		// split list in half
		// important, only future for one side so that tasks dont get submitted at rate of n^2, just n
		// also more efficient as stops would be the root thread from doing nothing while it waits
		CompletableFuture<List<T>> firstHalfCompletableFuture = CompletableFuture.supplyAsync(() -> MergeSort(list.subList(0, middle)) );
		List<T> secondHalf = MergeSort(list.subList(middle, list.size()));

		// not much point in sequentially merging from here as this thread would just be waiting
		return MSequentialSorter.merge(firstHalfCompletableFuture.join(), secondHalf);
	}

}
