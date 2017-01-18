import java.util.*;
import java.io.*;

public class Upvotes {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner console = new Scanner(System.in);
		int N = console.nextInt();
		int K = console.nextInt();
		int[] arr = new int[N];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = console.nextInt();
		}
		findDifference(N, K, arr);
	}

	public static void findDifference(int N, int K, int[] arr) {

		List<Range> nincRanges = preprocessArr(0, K, arr, true);
		List<Range> ndecRanges = preprocessArr(0, K, arr, false);

		long nsumInc = 0;
		long nsumDec = 0;

		for (int i = 0; i < nincRanges.size(); i++) {
			nsumInc += nincRanges.get(i).getCount();
		}

		for (int i = 0; i < ndecRanges.size(); i++) {
			nsumDec += ndecRanges.get(i).getCount();
		}
		System.out.println(nsumDec - nsumInc);
		// System.out.println(ndecRanges);
		// System.out.println(nincRanges);
		
		int p1 = 1;
		int p2 = K;

		while (p2 != N) {
			nsumInc = shiftWindow(p1, p2, nsumInc, true, nincRanges, arr);
			nsumDec = shiftWindow(p1, p2, nsumDec, false, ndecRanges, arr);
			System.out.println(nsumDec - nsumInc);
			// System.out.println(ndecRanges);
			// System.out.println(nincRanges);
			p1++;
			p2++;
		}

	}

	public static List<Range> preprocessArr(int beg, int K, int[] arr, boolean nonIncrease) {
		List<Range> ans = new LinkedList<Range>();
		int start = beg;
		int end = beg;
		for (int i = beg + 1; i < beg + K; i++) {
			if (!nonIncrease && arr[end] <= arr[i]) {
				end = i;
			} else if (nonIncrease && arr[end] >= arr[i]) {
				end = i;
			} else {
				if (start != end) {
					ans.add(new Range(start, end));
				}
				start = i;
				end = i;
			}
		}
		if (start != end) {
			ans.add(new Range(start, end));
		}
		return ans;
	}

	public static long shiftWindow(int p1, int p2, long prevSum, boolean nonIncrease, List<Range> range, int[] arr) {
		if (range.size() == 0) {
			List<Range> r = preprocessArr(p1, p2 - p1 + 1, arr, nonIncrease);
			if (r.size() == 0) {
				return 0;
			}
			for (int i = 0; i < r.size(); i++) {
				range.add(r.get(i));
			}
			long ans = 0;
			for (int i = 0; i < r.size(); i++) {
				ans += r.get(i).getCount();
			}
			return ans;
		}
		Range first = range.get(0);
		Range last = range.get(range.size() - 1);
		long ans = prevSum;
		if (first.startIndx == p1 - 1) {
			ans -= first.endIndx - first.startIndx;
			first.startIndx++;
		}

		if (last.endIndx == p2 - 1) {
			if (nonIncrease && arr[p2] <= arr[last.endIndx]) {
				ans += (last.endIndx - last.startIndx + 1);
				last.endIndx++;
			} else if (!nonIncrease && arr[p2] >= arr[last.endIndx]) {
				ans += (last.endIndx - last.startIndx + 1);
				last.endIndx++;
			}
			range.set(range.size() - 1, last);
		} else {
			if (nonIncrease && arr[p2] <= arr[p2-1]) {
				range.add(new Range(p2-1, p2));
				ans += 1;
			} else if (!nonIncrease && arr[p2] >= arr[p2-1]) {
				range.add(new Range(p2-1, p2));
				ans += 1;
			}
		}

		range.set(0, first);
		if (range.get(0).endIndx == range.get(0).startIndx) {
			range.remove(0);
		}

		return ans;
	}

	static class Range {
		
		public int startIndx;
		public int endIndx;

		public Range(int startIndx, int endIndx) {
			this.startIndx = startIndx;
			this.endIndx = endIndx;
		}

		public long getCount() {
			if (endIndx <= startIndx) {
				return 0;
			}
			long K = endIndx - startIndx + 1;
			K = K * (K-1);
			K = K/2;
			return K;
		}

		public String toString() {
			return "[" + startIndx + "," + " " + endIndx + "]";
		}

	}
}