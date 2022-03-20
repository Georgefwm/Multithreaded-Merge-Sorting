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
			if(this.job.size() < 20) return MSequentialSorter.mergeSort(job);
			
			int middle = this.job.size()/2;
			
			ForkingMergeSort<T> firstHalf = new ForkingMergeSort<T>(this.job.subList(0, middle));
			firstHalf.fork();
			ForkingMergeSort<T> secondHalf = new ForkingMergeSort<T>(this.job.subList(middle, this.job.size()));
			List<T> secondHalfSorted = secondHalf.compute();
			
			
	        return MSequentialSorter.merge(firstHalf.join(), secondHalfSorted);
		}

	}

}