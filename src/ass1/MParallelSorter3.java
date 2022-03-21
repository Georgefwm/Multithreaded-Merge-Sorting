package ass1;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author George McLachlan
 *
 */
public class MParallelSorter3 implements Sorter {
	public ForkJoinPool pool = new ForkJoinPool();
	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		return pool.invoke(new ForkingMergeSort<>(list));
	}


	@SuppressWarnings("serial")
	class ForkingMergeSort<T extends Comparable<? super T>> extends RecursiveTask<List<T>> {
		List<T> job;

		public ForkingMergeSort(List<T> list){
			this.job = list;	
		}

		protected List<T> compute() {
			
			// efficient to just complete in this thread if list is this small
			if(this.job.size() < 20) return MSequentialSorter.mergeSort(job);
			
			int middle = this.job.size()/2;
			
			// split list in half
			// only allocate a new thread for one side so the current thread can keep working
			ForkingMergeSort<T> firstHalf = new ForkingMergeSort<T>(this.job.subList(0, middle));
			firstHalf.fork();
			
			ForkingMergeSort<T> secondHalf = new ForkingMergeSort<T>(this.job.subList(middle, this.job.size()));
			List<T> secondHalfSorted = secondHalf.compute();
			
			
	        return MSequentialSorter.merge(firstHalf.join(), secondHalfSorted);
		}

	}

}