package algorithms;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;

public class AlgPart2 {
    public static void main(String[] args) {
        System.out.println("Max area start");
        System.out.println("Result: " + maxArea(new int[]{1, 5, 7}));
        System.out.println("Max area end");
        System.out.println("Min path sum start");
        System.out.println("Result: " + minPathSum(new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}}));
        System.out.println("Min path sum end");
        System.out.println("Pacific Atlantic start");
        System.out.println("Result: " + pacificAtlantic(new int[][]{{1, 2, 2, 3, 5}, {3, 2, 3, 4, 4}, {2, 4, 5, 3, 1}, {6, 7, 1, 4, 5}, {5, 1, 1, 2, 4}}));
        System.out.println("Pacific Atlantic  end");
    }

    public static int maxArea(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;
        while (left < right) {
            int area = getArea(left, right, height);
            maxArea = Math.max(maxArea, area);

            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }

    public static int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 1; i < m; i++) {
            grid[i][0] += grid[i - 1][0];
        }
        for (int i = 1; i < n; i++) {
            grid[0][i] += grid[0][i - 1];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                grid[i][j] += Math.min(grid[i][j - 1], grid[i - 1][j]);
            }
        }

        return grid[m - 1][n - 1];
    }

    public static int magicalString(int n) {
        StringBuilder s = new StringBuilder("122");
        int pointer = 2;
        int count = 1;
        boolean is1 = true;

        while (s.length() < n) {
            int last = s.charAt(pointer) - '0';
            char toAdd = is1 ? '1' : '2';
            if (is1) {
                count += last;
            }
            for (int i = 0; i < last; i++) {
                s.append(toAdd);
            }

            if (s.length() > n && is1) {
                count--;
            }

            pointer++;
            is1 = !is1;
        }

        return count;
    }

    public static String frequencySort(String s) {
        StringBuilder result = new StringBuilder();
        int[][] charsToCount = new int[123][2];
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - '0';
            charsToCount[c][0] = c;
            charsToCount[c][1]++;
        }
        charsToCount = Arrays.stream(charsToCount).sorted((a1, a2) -> a1[1] - a2[1]).toArray(int[][]::new);
        for (int i = charsToCount.length - 1; i >= 0; i--) {
            int repeat = charsToCount[i][1];
            if (repeat == 0) break;
            int ch = charsToCount[i][0];
            while (repeat > 0) {
                char c = (char) (ch + '0');
                result.append(c);
                repeat--;
            }
        }

        return result.toString();
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Stack<Integer> l1Stack = new Stack<>();
        Stack<Integer> l2Stack = new Stack<>();

        ListNode l1Temp = l1;
        while (l1Temp != null) {
            l1Stack.push(l1Temp.val);
            l1Temp = l1Temp.next;
        }

        ListNode l2Temp = l2;
        while (l2Temp != null) {
            l2Stack.push(l2Temp.val);
            l2Temp = l2Temp.next;
        }

        int add = 0;
        Stack<Integer> resultStack = new Stack<>();
        while (!l1Stack.isEmpty() && !l2Stack.isEmpty()) {
            int l1Int = l1Stack.pop();
            int l2Int = l2Stack.pop();

            int sum = l1Int + l2Int + add;

            if (sum > 9) {
                add = 1;
                resultStack.push(sum - 10);
            } else {
                add = 0;
                resultStack.push(sum);
            }
        }

        while (!l1Stack.isEmpty()) {
            int sum = l1Stack.pop() + add;
            if (sum > 9) {
                add = 1;
                resultStack.push(sum - 10);
            } else {
                add = 0;
                resultStack.push(sum);
            }
        }

        while (!l2Stack.isEmpty()) {
            int sum = l2Stack.pop() + add;
            if (sum > 9) {
                add = 1;
                resultStack.push(sum - 10);
            } else {
                add = 0;
                resultStack.push(sum);
            }
        }

        if (add == 1) {
            resultStack.push(add);
        }

        ListNode result = new ListNode(resultStack.pop());
        ListNode temp = result;

        while (!resultStack.isEmpty()) {
            ListNode next = new ListNode(resultStack.pop());
            temp.next = next;
            temp = next;
        }

        return result;
    }

    public static int superPow(int a, int[] b) {
        BigInteger aBig = BigInteger.valueOf((long) a);
        BigInteger modBig = BigInteger.valueOf(1337L);
        StringBuilder bStr = new StringBuilder();

        for (int i = 0; i < b.length; i++) {
            bStr.append(b[i]);
        }

        BigInteger bBig = new BigInteger(bStr.toString());
        return aBig.modPow(bBig, modBig).intValue();
    }

    public static List<List<Integer>> pacificAtlantic(int[][] heights) {
        int rowsLen = heights.length;
        int colsLen = heights[0].length;
        boolean[][] pac = new boolean[rowsLen][colsLen];
        boolean[][] atl = new boolean[rowsLen][colsLen];

        for (int row = 0; row < rowsLen; row++) {
            dfs(row, 0, rowsLen, colsLen, pac, heights[row][0], heights);
            dfs(row, colsLen - 1, rowsLen, colsLen, atl, heights[row][colsLen - 1], heights);
        }

        for (int col = 0; col < colsLen; col++) {
            dfs(0, col, rowsLen, colsLen, pac, heights[0][col], heights);
            dfs(rowsLen - 1, col, rowsLen, colsLen, atl, heights[rowsLen - 1][col], heights);
        }

        return IntStream.range(0, rowsLen)
                .boxed()
                .flatMap(row -> IntStream.range(0, colsLen)
                        .filter(col -> pac[row][col] && atl[row][col])
                        .mapToObj(col -> List.of(row, col)))
                .toList();
    }

    private static void dfs(int row, int col, int rowsLen, int colsLen, boolean[][] visited, int prevHeight, int[][] heights) {
        if (row < 0 || row >= rowsLen || col < 0 || col >= colsLen || visited[row][col] || prevHeight > heights[row][col]) {
            return;
        }

        visited[row][col] = true;
        dfs(row, col + 1, rowsLen, colsLen, visited, heights[row][col], heights);
        dfs(row + 1, col, rowsLen, colsLen, visited, heights[row][col], heights);
        dfs(row, col - 1, rowsLen, colsLen, visited, heights[row][col], heights);
        dfs(row - 1, col, rowsLen, colsLen, visited, heights[row][col], heights);
    }

    private static int getArea(int leftEndpointIndex, int rightEndpointIndex, int[] height) {
        return (rightEndpointIndex - leftEndpointIndex)
                * Math.min(height[leftEndpointIndex], height[rightEndpointIndex]);
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
