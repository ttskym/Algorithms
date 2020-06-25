/**
 * Simple segment tree implementation that supports sum range queries and point updates.
 *
 * <p>NOTE: This file is still a little bit of a WIP
 *
 * <p>Run with: ./gradlew run
 * -Palgorithm=datastructures.segmenttree.RangeQueryPointUpdateSegmentTree
 *
 * <p>Several thanks to cp-algorithms for their great article on segment trees:
 * https://cp-algorithms.com/data_structures/segment_tree.html
 *
 * @author William Fiset, william.alexandre.fiset@gmail.com
 */
package com.williamfiset.algorithms.datastructures.segmenttree;

import java.util.Arrays;
import java.util.function.BinaryOperator;

public class RangeQueryPointUpdateSegmentTree {

  // The type of segment combination function to use
  public static enum Operation {
    SUM,
    MIN,
    MAX
  }

  // The number of values in the original input values array.
  private int n;

  private long[] t;

  private Operation op;

  // The chosen range combination function
  private BinaryOperator<Long> fn;

  private BinaryOperator<Long> sumFn = (a, b) -> a + b;
  private BinaryOperator<Long> minFn = (a, b) -> Math.min(a, b);
  private BinaryOperator<Long> maxFn = (a, b) -> Math.max(a, b);

  public RangeQueryPointUpdateSegmentTree(long[] values, Operation op) {
    if (values == null) {
      throw new NullPointerException("Segment tree values cannot be null.");
    }
    if (op == null) {
      throw new NullPointerException("Please specify a valid segment combination operation.");
    }
    n = values.length;
    this.op = op;

    // The size of the segment tree `t`
    //
    // TODO(william): Investigate to reduce this space. There are only 2n-1 segments, so we should
    // be able to reduce the space, but may need to reorganize the tree/queries. One idea is to use
    // the Eulerian tour structure of the tree to densely pack the segments.
    int N = 4 * n;

    t = new long[N];

    if (op == Operation.SUM) {
      fn = sumFn;
    } else if (op == Operation.MIN) {
      Arrays.fill(t, Long.MAX_VALUE);
      fn = minFn;
    } else if (op == Operation.MAX) {
      Arrays.fill(t, Long.MIN_VALUE);
      fn = maxFn;
    }

    buildSegmentTree(0, 0, n - 1, values);
  }

  /**
   * Builds a segment tree by starting with the leaf nodes and combining segment values on callback.
   *
   * @param i the index of the segment in the segment tree
   * @param l the left index (inclusive) of the range in the values array
   * @param r the right index (inclusive) of the range in the values array
   * @param values the initial values array
   */
  private void buildSegmentTree(int i, int tl, int tr, long[] values) {
    if (tl == tr) {
      t[i] = values[tl];
      return;
    }
    int mid = (tl + tr) / 2;
    buildSegmentTree(2 * i + 1, tl, mid, values);
    buildSegmentTree(2 * i + 2, mid + 1, tr, values);

    t[i] = fn.apply(t[2 * i + 1], t[2 * i + 2]);
  }

  /**
   * Returns the query of the range [l, r] on the original `values` array (+ any updates made to it)
   *
   * @param l the left endpoint of the range query (inclusive)
   * @param r the right endpoint of the range query (inclusive)
   */
  public long rangeQuery(int l, int r) {
    return rangeQuery(0, 0, n - 1, l, r);
  }

  /**
   * Returns the query of the range [l, r] on the original `values` array (+ any updates made to it)
   *
   * @param l the left endpoint of the range query (inclusive)
   * @param r the right endpoint of the range query (inclusive)
   */
  public long rangeQuery2(int l, int r) {
    return rangeQuery2(0, 0, n - 1, l, r);
  }

  /**
   * @param i the index of the current segment in the tree
   * @param tl the left endpoint that the of the current segment
   * @param tr the right endpoint that the of the current segment
   * @param l the target left endpoint for the range query
   * @param r the target right endpoint for the range query
   */
  private long rangeQuery(int i, int tl, int tr, int l, int r) {
    if (l > r) {
      // Different segment tree types have different base cases:
      if (op == Operation.SUM) {
        return 0;
      } else if (op == Operation.MIN) {
        return Long.MAX_VALUE;
      } else if (op == Operation.MAX) {
        return Long.MIN_VALUE;
      }
    }
    if (tl == l && tr == r) {
      return t[i];
    }
    int tm = (tl + tr) / 2;
    // Instead of checking if [tl, tm] overlaps [l, r] and [tm+1, tr] overlaps
    // [l, r], simply recurse on both and return a sum of 0 if the interval is invalid.
    return fn.apply(
        rangeQuery(2 * i + 1, tl, tm, l, Math.min(tm, r)),
        rangeQuery(2 * i + 2, tm + 1, tr, Math.max(l, tm + 1), r));
  }

  // Alternative implementation of summing that intelligently only digs into
  // the branches which overlap with the query [l, r].
  //
  // This version of the range query impl also has the advantage that it doesn't
  // need to know the explicit base case value for each query type.
  private long rangeQuery2(int i, int tl, int tr, int l, int r) {
    if (tl == l && tr == r) {
      return t[i];
    }
    int tm = (tl + tr) / 2;
    // Test how the current segment [tl, tr] overlaps with the query [l, r]
    boolean overlapsLeftSegment = (l <= tm);
    boolean overlapsRightSegment = (r > tm);
    if (overlapsLeftSegment && overlapsRightSegment) {
      return fn.apply(
          rangeQuery2(2 * i + 1, tl, tm, l, Math.min(tm, r)),
          rangeQuery2(2 * i + 2, tm + 1, tr, Math.max(l, tm + 1), r));
    } else if (overlapsLeftSegment) {
      return rangeQuery2(2 * i + 1, tl, tm, l, Math.min(tm, r));
    } else {
      return rangeQuery2(2 * i + 2, tm + 1, tr, Math.max(l, tm + 1), r);
    }
  }

  // Updates the segment tree to reflect that index `i` in the original `values` array was updated
  // to `newValue`.
  public void update(int i, long newValue) {
    update(0, i, 0, n - 1, newValue);
  }

  /**
   * Update a point segment to a new value and update all affected segments.
   *
   * <p>Do this by performing a binary search to find the interval containing the point, then update
   * the leaf segment with the new value, and re-compute all affected segment values on the
   * callback.
   *
   * @param at the index of the current segment in the tree
   * @param pos the target position to update
   * @param tl the left segment endpoint
   * @param tr the right segment endpoint
   * @param newValue the new value to update
   */
  private void update(int at, int pos, int tl, int tr, long newValue) {
    if (tl == tr) { // `tl == pos && tr == pos` might be clearer
      t[at] = newValue;
      return;
    }
    int tm = (tl + tr) / 2;
    // The point index `pos` is contained within the left segment [tl, tm]
    if (pos <= tm) {
      update(2 * at + 1, pos, tl, tm, newValue);
      // The point index `pos` is contained within the right segment [tm+1, tr]
    } else {
      update(2 * at + 2, pos, tm + 1, tr, newValue);
    }
    // Re-compute the segment value of the current segment on the callback
    t[at] = fn.apply(t[2 * at + 1], t[2 * at + 2]);
  }

  ////////////////////////////////////////////////////
  //              Example usage:                    //
  ////////////////////////////////////////////////////

  public static void main(String[] args) {
    rangeSumQueryExample();
    rangeMinQueryExample();
    rangeMaxQueryExample();
  }

  private static void rangeSumQueryExample() {
    //               0  1  2  3
    long[] values = {1, 2, 3, 2};
    RangeQueryPointUpdateSegmentTree st =
        new RangeQueryPointUpdateSegmentTree(values, Operation.SUM);

    int l = 0, r = 3;
    System.out.printf("The sum between indeces [%d, %d] is: %d\n", l, r, st.rangeQuery(l, r));
    // Prints:
    // The sum between indeces [0, 3] is: 8
  }

  private static void rangeMinQueryExample() {
    //               0  1  2  3
    long[] values = {1, 2, 3, 2};
    RangeQueryPointUpdateSegmentTree st =
        new RangeQueryPointUpdateSegmentTree(values, Operation.MIN);

    int l = 0, r = 3;
    System.out.printf("The sum between indeces [%d, %d] is: %d\n", l, r, st.rangeQuery(l, r));
    // Prints:
    // The sum between indeces [0, 3] is: 1
  }

  private static void rangeMaxQueryExample() {
    //               0  1  2  3
    long[] values = {1, 2, 3, 2};
    RangeQueryPointUpdateSegmentTree st =
        new RangeQueryPointUpdateSegmentTree(values, Operation.MAX);

    int l = 0, r = 3;
    System.out.printf("The sum between indeces [%d, %d] is: %d\n", l, r, st.rangeQuery(l, r));
    // Prints:
    // The sum between indeces [0, 3] is: 3
  }
}
