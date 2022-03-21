package ass1;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author George McLachlan
 *
 */
public class MParallelSorter1 implements Sorter {
	private ExecutorService threadPool = Executors.newWorkStealingPool();

	
	/**
	 * The benefit of this algorithm is that it is takes advantage of multiple threads. When used with a task like 
	 * sorting, it becomes very apparent that multithreading is one of the best ways to speed up the execution. Multithreading 
	 * is better for sorting large lists, especially if you have a low time requirement to meet, in some cases if 
	 * the algorithm is too slow it's not a viable option for the project. Its important to note that multithreading is 
	 * convenient for the mergesort algorithm as no data is simultaneously accessed by different threads, this seriously 
	 * reduces the complexity that would probably occur if n threads were trying to access and modify the same variables 
	 * at the same time. 
	 * <p>
	 * 
	 * I learned how to implement Futures to delegate a task to a 'worker' in the thread pool. When implementing multithreading I
	 * realized that it's important to understand how important splitting tasks in the right places is; When done wrong, there isnt
	 * much performance increase.
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
	 * Gets recursively called to split up the original list into smaller pieces and delegates tasks to new threads if needed
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
	private <T> T get(Future<T> future) {
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			throw new Error("Unexpected Checked Excepton", e.getCause());
		}
	}
}
