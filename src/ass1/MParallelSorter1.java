package ass1;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author George McLachlan
 *
 */
public class MParallelSorter1 implements Sorter{
	private ExecutorService threadPool = Executors.newWorkStealingPool();


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
	 * @param list to be sorted. 
	 * @return Sorted list. In the last case, the final sorted list.
	 */
	private <T extends Comparable<? super T>> List<T> mergeSort(List<T> list){

		// efficient to just complete in this thread if list is this small
		if(list.size() < 20) return MSequentialSorter.mergeSort(list);

		int middle = list.size()/2;

		// split list in half
		// only allocate a new thread for one side so the current thread can keep working
		Future<List<T>> firstHalfFuture = threadPool.submit( () -> mergeSort(list.subList(0, middle)) );
		List<T> secondHalf = mergeSort(list.subList(middle, list.size()));

		return MSequentialSorter.merge(get(firstHalfFuture), secondHalf);
	}


	/**
	 * Dedicated method for getting Future result. Handles exceptions.
	 * 
	 * Makes readability of other methods better when you dont have try/catch blocks above.
	 *
	 * @param future Given task
	 * @return Result of task
	 */
	private <T> T get(Future<T> future){
		try{
			return future.get();
		}catch(InterruptedException | ExecutionException e){
			e.printStackTrace();
			throw new Error("Unexpected Checked Excepton", e.getCause());
		}
	}
}
