/**
 * gradle test --info --tests
 * "com.williamfiset.algorithms.datastructures.segmenttree.RangeQueryPointUpdateSegmentTreeTest"
 */
package com.williamfiset.algorithms.datastructures.segmenttree;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;

import com.williamfiset.algorithms.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

public class RangeQueryPointUpdateSegmentTreeTest {

  @Before
  public void setup() {}

  @Test
  public void testSumQuery() {
    long[] values = {1, 2, 3, 4, 5};
    RangeQueryPointUpdateSegmentTree st =
        new RangeQueryPointUpdateSegmentTree(
            values, RangeQueryPointUpdateSegmentTree.Operation.SUM);

    assertThat(st.rangeQuery(0, 1)).isEqualTo(3);
    assertThat(st.rangeQuery(2, 2)).isEqualTo(3);
    assertThat(st.rangeQuery(0, 4)).isEqualTo(15);
  }

  @Test
  public void testAllSumQueries() {
    int n = 100;
    long[] ar = TestUtils.randomLongArray(n, -1000, +1000);
    RangeQueryPointUpdateSegmentTree st =
        new RangeQueryPointUpdateSegmentTree(ar, RangeQueryPointUpdateSegmentTree.Operation.SUM);

    for (int i = 0; i < n; i++) {
      for (int j = i; j < n; j++) {
        long bfSum = bruteForceSum(ar, i, j);
        long segTreeSum = st.rangeQuery(i, j);
        assertThat(bfSum).isEqualTo(segTreeSum);
      }
    }
  }

  @Test
  public void testRandomPointUpdatesAndSumRangeQueries() {
    int n = 100;
    long[] ar = TestUtils.randomLongArray(n, -1000, +1000);
    RangeQueryPointUpdateSegmentTree st =
        new RangeQueryPointUpdateSegmentTree(ar, RangeQueryPointUpdateSegmentTree.Operation.SUM);

    for (int i = 0; i < n; i++) {
      for (int j = i; j < n; j++) {
        // Range query
        long bfSum = bruteForceSum(ar, i, j);
        long segTreeSum = st.rangeQuery(i, j);
        assertThat(bfSum).isEqualTo(segTreeSum);

        // Point update
        int randIndex = TestUtils.randValue(i, j + 1);
        long randValue = TestUtils.randValue(-1000, 1000);
        st.update(randIndex, randValue);
        ar[randIndex] = randValue;
        bfSum = bruteForceSum(ar, i, j);
        segTreeSum = st.rangeQuery(i, j);
        assertThat(bfSum).isEqualTo(segTreeSum);
      }
    }
  }

  @Test
  public void testRandomPointUpdatesAndMinRangeQueries() {
    int n = 100;
    long[] ar = TestUtils.randomLongArray(n, -1000, +1000);
    RangeQueryPointUpdateSegmentTree st =
        new RangeQueryPointUpdateSegmentTree(ar, RangeQueryPointUpdateSegmentTree.Operation.MIN);

    for (int i = 0; i < n; i++) {
      for (int j = i; j < n; j++) {
        // Range query
        long bfMin = bruteForceMin(ar, i, j);
        long segTreeMin = st.rangeQuery(i, j);
        assertThat(bfMin).isEqualTo(segTreeMin);

        // Point update
        int randIndex = TestUtils.randValue(i, j + 1);
        long randValue = TestUtils.randValue(-1000, 1000);
        st.update(randIndex, randValue);
        ar[randIndex] = randValue;
        bfMin = bruteForceMin(ar, i, j);
        segTreeMin = st.rangeQuery(i, j);
        assertThat(bfMin).isEqualTo(segTreeMin);
      }
    }
  }

  @Test
  public void testRandomPointUpdatesAndMaxRangeQueries() {
    int n = 100;
    long[] ar = TestUtils.randomLongArray(n, -1000, +1000);
    RangeQueryPointUpdateSegmentTree st =
        new RangeQueryPointUpdateSegmentTree(ar, RangeQueryPointUpdateSegmentTree.Operation.MAX);

    for (int i = 0; i < n; i++) {
      for (int j = i; j < n; j++) {
        // Range query
        long bfMax = bruteForceMax(ar, i, j);
        long segTreeMax = st.rangeQuery(i, j);
        assertThat(bfMax).isEqualTo(segTreeMax);

        // Point update
        int randIndex = TestUtils.randValue(i, j + 1);
        long randValue = TestUtils.randValue(-1000, 1000);
        st.update(randIndex, randValue);
        ar[randIndex] = randValue;
        bfMax = bruteForceMax(ar, i, j);
        segTreeMax = st.rangeQuery(i, j);
        assertThat(bfMax).isEqualTo(segTreeMax);
      }
    }
  }

  @Test
  public void testRandomPointUpdatesAndSumRangeQueries_rangeQuery2() {
    int n = 100;
    long[] ar = TestUtils.randomLongArray(n, -1000, +1000);
    RangeQueryPointUpdateSegmentTree st =
        new RangeQueryPointUpdateSegmentTree(ar, RangeQueryPointUpdateSegmentTree.Operation.SUM);

    for (int i = 0; i < n; i++) {
      for (int j = i; j < n; j++) {
        // Range query
        long bfSum = bruteForceSum(ar, i, j);
        long segTreeSum = st.rangeQuery(i, j);
        assertThat(bfSum).isEqualTo(segTreeSum);

        // Point update
        int randIndex = TestUtils.randValue(i, j + 1);
        long randValue = TestUtils.randValue(-1000, 1000);
        st.update(randIndex, randValue);
        ar[randIndex] = randValue;
        bfSum = bruteForceSum(ar, i, j);
        segTreeSum = st.rangeQuery2(i, j);
        assertThat(bfSum).isEqualTo(segTreeSum);
      }
    }
  }

  @Test
  public void testRandomPointUpdatesAndMinRangeQueries_rangeQuery2() {
    int n = 100;
    long[] ar = TestUtils.randomLongArray(n, -1000, +1000);
    RangeQueryPointUpdateSegmentTree st =
        new RangeQueryPointUpdateSegmentTree(ar, RangeQueryPointUpdateSegmentTree.Operation.MIN);

    for (int i = 0; i < n; i++) {
      for (int j = i; j < n; j++) {
        // Range query
        long bfMin = bruteForceMin(ar, i, j);
        long segTreeMin = st.rangeQuery2(i, j);
        assertThat(bfMin).isEqualTo(segTreeMin);

        // Point update
        int randIndex = TestUtils.randValue(i, j + 1);
        long randValue = TestUtils.randValue(-1000, 1000);
        st.update(randIndex, randValue);
        ar[randIndex] = randValue;
        bfMin = bruteForceMin(ar, i, j);
        segTreeMin = st.rangeQuery2(i, j);
        assertThat(bfMin).isEqualTo(segTreeMin);
      }
    }
  }

  @Test
  public void testRandomPointUpdatesAndMaxRangeQueries_rangeQuery2() {
    int n = 100;
    long[] ar = TestUtils.randomLongArray(n, -1000, +1000);
    RangeQueryPointUpdateSegmentTree st =
        new RangeQueryPointUpdateSegmentTree(ar, RangeQueryPointUpdateSegmentTree.Operation.MAX);

    for (int i = 0; i < n; i++) {
      for (int j = i; j < n; j++) {
        // Range query
        long bfMax = bruteForceMax(ar, i, j);
        long segTreeMax = st.rangeQuery(i, j);
        assertThat(bfMax).isEqualTo(segTreeMax);

        // Point update
        int randIndex = TestUtils.randValue(i, j + 1);
        long randValue = TestUtils.randValue(-1000, 1000);
        st.update(randIndex, randValue);
        ar[randIndex] = randValue;
        bfMax = bruteForceMax(ar, i, j);
        segTreeMax = st.rangeQuery(i, j);
        assertThat(bfMax).isEqualTo(segTreeMax);
      }
    }
  }

  // Finds the sum in an array between [l, r] in the `values` array
  private static long bruteForceSum(long[] values, int l, int r) {
    long s = 0;
    for (int i = l; i <= r; i++) {
      s += values[i];
    }
    return s;
  }

  // Finds the min value in an array between [l, r] in the `values` array
  private static long bruteForceMin(long[] values, int l, int r) {
    long m = values[l];
    for (int i = l; i <= r; i++) {
      m = Math.min(m, values[i]);
    }
    return m;
  }

  // Finds the max value in an array between [l, r] in the `values` array
  private static long bruteForceMax(long[] values, int l, int r) {
    long m = values[l];
    for (int i = l; i <= r; i++) {
      m = Math.max(m, values[i]);
    }
    return m;
  }
}
