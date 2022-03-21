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
	 * is so much better for sorting large lists, especially if you have a low time requirement to meet, in some cases if 
	 * the algorithm is too slow it's not a viable option for the project. Its important to note that multithreading is 
	 * perfect for the mergesort algorithm as no data is simultaneously accessed by different threads, this seriously 
	 * reduces the complexity that would probably occur if n threads were trying to access and modify the same variables 
	 * at the same time. 
	 * 
	 * <p>
	 * I learned how to implement a multitrhreaded veriation of the merge sort algorithm. 
	 * 
	 * <p>
	 * @param List<T> to be sorted
	 * @return The Sorted List<T>
	 */
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
		
		// efficient to just complete in this thread if list is this small
		if(list.size() < 20) return MSequentialSorter.mergeSort(list);

		int middle = list.size()/2;

		// split list in half
		// only allocate a new thread for one side so that the current thread can keep working
		Future<List<T>> firstHalfFuture = threadPool.submit( () -> MergeSort(list.subList(0, middle)) );
		List<T> secondHalf = MergeSort(list.subList(middle, list.size()));

		return MSequentialSorter.merge(get(firstHalfFuture), secondHalf);
	}


	/**
	 * Dedicated method for getting Future result
	 * 
	 * Saw someone online use something similar, makes readability of other methods better when you dont have try/catch
	 * blocks.
	 *
	 * @param Future<T> task
	 * 
	 * @return Future<T> result
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
