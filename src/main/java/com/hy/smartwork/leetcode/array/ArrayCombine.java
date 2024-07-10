package com.hy.smartwork.leetcode.array;

/**
 * @author huangying1
 */
public class ArrayCombine {

    /**
     * 已通过
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        if (n == 0) {
            return;
        }
        if (m == 0) {
            System.arraycopy(nums2, 0, nums1, 0, nums1.length);
            return;
        }

        // 思路：创建一个临时数组，nums1和nums2数组通过比较大小进行填充
        int[] temp = new int[nums1.length];
        int index1 = 0;
        int index2 = 0;
        for (int i = 0; i < nums1.length; i++) {
            if (index1 >= m) {
                temp[i] = nums2[index2];
                index2++;
                continue;
            }
            if (index2 >= n) {
                temp[i] = nums1[index1];
                index1++;
                continue;
            }
            if (index1 < m && index2 < n && nums1[index1] <= nums2[index2]) {
                temp[i] = nums1[index1];
                index1++;
            } else {
                temp[i] = nums2[index2];
                index2++;
            }
        }
        // 数组拷贝
        System.arraycopy(temp, 0, nums1, 0, temp.length);
    }

    /**
     * 已通过
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void merge2(int[] nums1, int m, int[] nums2, int n) {
        if (n == 0) {
            return;
        }
        if (m == 0) {
            System.arraycopy(nums2, 0, nums1, 0, nums1.length);
            return;
        }

        // 思路：比较大小移动nums1数组内的元素
        int j = 0;
        for (int i = 0; i < nums1.length; i++) {
            if(j >= n) {
                continue;
            }
            if (nums1[i] > nums2[j]) {
                // i 位置填充nums2的元素，nums1的后续元素向后移动1位
                for (int k = nums1.length - 1; k > i; k--) {
                    nums1[k] = nums1[k - 1];
                }
                nums1[i] = 0;
                nums1[i] = nums2[j];
                j++;
                continue;
            }
            if (i >= (m+j-1) && nums1[i] == 0) {
                // 判断是末尾填充0的逻辑
                // 超过了m的长度，nums1元素均为0，可以直接赋值
                nums1[i] = nums2[j];
                j++;
            }
        }
    }
}
