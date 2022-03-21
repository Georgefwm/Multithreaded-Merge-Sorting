package ass1;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class TestPerformance {

	/**
	 * Does some warmup runs then records the time taken when the JIT compiler is operating properly.
	 * We use Runnable here so we can use utilise threads without having write a new class that extends Thread.
	 * 
	 * @param r Runnable task
	 * @param warmUp Number of sorts to do before recording time results.
	 * @param runs Number of sort calls to do while recording time taken.
	 * @return Time taken to finish execution.
	 */
	long timeOf(Runnable r,int warmUp,int runs) {
		System.gc();
		for(int i=0;i<warmUp;i++) {r.run();}
		long time0=System.currentTimeMillis();
		for(int i=0;i<runs;i++) {r.run();}
		long time1=System.currentTimeMillis();
		return time1-time0;
	}

	/**
	 * Handles the testing of a single sorting variation. Prints the time taken by the parsed Sorter.
	 * 
	 * @param s sorter object to be used
	 * @param name Name of the sorter used, for nice console logging.
	 * @param dataset Dataset to use 
	 */
	<T extends Comparable<? super T>>void msg(Sorter s,String name,T[][] dataset) {
		long time=timeOf(()->{
			for(T[]l:dataset){s.sort(Arrays.asList(l));}
		},20000,200);//realistically 20.000 to make the JIT do his job..
		System.out.println(name+" sort takes "+time/1000d+" seconds");
	}

	/**
	 * Used to call every variation of the sorting methods, uses the same dataset for acurate comparrison.
	 * 
	 * @param dataset A 2D array which represents a series of lists for testing on
	 */
	<T extends Comparable<? super T>>void msgAll(T[][] dataset) {
		//msg(new ISequentialSorter(),"Sequential insertion",TestBigInteger.dataset); //so slow
		//uncomment the former line to include performance of ISequentialSorter
		msg(new MSequentialSorter(),"Sequential merge sort",dataset);
		msg(new MParallelSorter1(),"Parallel merge sort (futures)",dataset);
		msg(new MParallelSorter2(),"Parallel merge sort (completablefutures)",dataset);
		msg(new MParallelSorter3(),"Parallel merge sort (forkJoin)",dataset);
	}


	/**
	 * Tests with the BigInteger datatype
	 */
	@Test
	void testBigInteger() {
		System.out.println("On the data type BigInteger");
		msgAll(TestBigInteger.dataset);
	}

	/**
	 * Tests with the Float datatype
	 */
	@Test
	void testFloat() {
		System.out.println("On the data type Float");
		msgAll(TestFloat.dataset);
	}

	/**
	 * Tests with the Point datatype
	 */
	@Test
	void testPoint() {
		System.out.println("On the data type Point");
		msgAll(TestPoint.dataset);
	}

	/**
	 * Tests with the Double datatype
	 */
	@Test
	void testDouble() {
		System.out.println("On the data type Double");
		msgAll(TestDouble.dataset);
	}


}
/*
With the model solutions, on a lab machine (2019) we may get those results:
On the data type Float
Sequential merge sort sort takes 1.178 seconds
Parallel merge sort (futures) sort takes 0.609 seconds
Parallel merge sort (completablefutures) sort takes 0.403 seconds
Parallel merge sort (forkJoin) sort takes 0.363 seconds
On the data type Point
Sequential merge sort sort takes 1.373 seconds
Parallel merge sort (futures) sort takes 0.754 seconds
Parallel merge sort (completablefutures) sort takes 0.541 seconds
Parallel merge sort (forkJoin) sort takes 0.48 seconds
On the data type BigInteger
Sequential merge sort sort takes 1.339 seconds
Parallel merge sort (futures) sort takes 0.702 seconds
Parallel merge sort (completablefutures) sort takes 0.452 seconds
Parallel merge sort (forkJoin) sort takes 0.492 seconds

On another lab machine in 2021 we get those results:
//they must have optimized sequential execution quite a bit!
On the data type Float
Sequential merge sort sort takes 0.635 seconds
Parallel merge sort (futures) sort takes 0.475 seconds //with a smart trick we may get 0.241 instead
Parallel merge sort (completablefutures) sort takes 0.25 seconds
Parallel merge sort (forkJoin) sort takes 0.253 seconds
On the data type Point
Sequential merge sort sort takes 0.76 seconds //with a smart trick we may get 0.296 instead
Parallel merge sort (futures) sort takes 0.505 seconds
Parallel merge sort (completablefutures) sort takes 0.279 seconds
Parallel merge sort (forkJoin) sort takes 0.296 seconds
On the data type BigInteger
Sequential merge sort sort takes 0.871 seconds
Parallel merge sort (futures) sort takes 0.574 seconds //with a smart trick we may get 0.372 instead
Parallel merge sort (completablefutures) sort takes 0.354 seconds
Parallel merge sort (forkJoin) sort takes 0.338 seconds

 */