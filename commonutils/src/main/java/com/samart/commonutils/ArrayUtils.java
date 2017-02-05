package com.samart.commonutils;

import junit.framework.Assert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ArrayUtils {
	
	public static final int[] INTS = new int[0];
	
	public static <T> T[] emptyArray(final Class<T> componentType) {
		return newArray(componentType, 0);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(final Class<T> componentType, final int size) {
		Assert.assertNotNull(componentType);
		
		return (T[]) Array.newInstance(componentType, size);
	}
	
	@SuppressWarnings({ "unchecked" })
	public static <T> T[] concat(final T[] first, final T[] second) {
		if (first == null && second == null) {
			return null;
		} else if (isEmpty(first) && isEmpty(second)) {
			Assert.assertTrue(first != null || second != null);
			
			return (T[]) emptyArray(first != null
				? first.getClass().getComponentType()
				: second.getClass().getComponentType());
		} else if (!isEmpty(first) && !isEmpty(second)) {
			final T[] result = (T[]) Array.newInstance(
				first.getClass().getComponentType(), first.length + second.length);
			
			System.arraycopy(first, 0, result, 0, first.length);
			System.arraycopy(second, 0, result, first.length, second.length);
			
			return result;
		} else if (second == null) {
			Assert.assertNotNull(first);
			
			return first.length > 0
				? Arrays.copyOf(first, first.length)
				: (T[]) emptyArray(first.getClass().getComponentType());
		} else if (first == null) {
			Assert.assertNotNull(second);
			
			return second.length > 0
				? Arrays.copyOf(second, second.length)
				: (T[]) emptyArray(second.getClass().getComponentType());
		} else if (isEmpty(second)) {
			Assert.assertNotNull(first);
			Assert.assertTrue(first.length > 0);
			
			return Arrays.copyOf(first, first.length);
		} else /*if (isEmpty(first))*/ {
			Assert.assertNotNull(second);
			Assert.assertTrue(second.length > 0);
			
			return Arrays.copyOf(second, second.length);
		}
	}
	
	public static int indexOf(final boolean[] array, final boolean value) {
		if (!isEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		
		return -1;
	}
	
	public static int indexOf(final int[] array, final int value) {
		if (!isEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		
		return -1;
	}
	
	public static int indexOf(final long[] array, final long value) {
		if (!isEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		
		return -1;
	}
	
	public static int indexOf(final float[] array, final float value) {
		if (!isEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		
		return -1;
	}
	
	public static int indexOf(final double[] array, final double value) {
		if (!isEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		
		return -1;
	}
	
	public static <T> int indexOf(final T[] array, final T value) {
		if (!isEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i] || (value != null && value.equals(array[i]))) {
					return i;
				}
			}
		}
		
		return -1;
	}
	
	public static int indexOfSubArray(final int[][] array, final int[] subArray) {
		if (!isEmpty(array) && !isEmpty(subArray)) {
			for (int i = 0; i < array.length; i++) {
				if (Arrays.equals(array[i], subArray)) {
					return i;
				}
			}
		}
		
		return -1;
	}
	
	public static <T extends Enum<?>> int indexOf(final T[] array, final T value) {
		if (!isEmpty(array) && value != null) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		
		return -1;
	}
	
	/**
	 * <p>
	 * Removes the element at the specified position from the specified array.
	 * All subsequent elements are shifted to the left (substracts one from
	 * their indices).
	 * </p>
	 * <p>
	 * <p>
	 * This method returns a new array with the same elements of the input array
	 * except the element on the specified position. The component type of the
	 * returned array is always the same as that of the input array.
	 * </p>
	 * <p>
	 * <p>
	 * If the input array is <code>null</code>, an IndexOutOfBoundsException
	 * will be thrown, because in that case no valid index can be specified.
	 * </p>
	 *
	 * @param array the array to remove the element from, may not be
	 *              <code>null</code>
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element
	 * at the specified position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >=
	 *                                   array.length), or if the array is <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] remove(final T[] array, final int index) {
		final int length = getLength(array);
		
		if (length == 0 || index < 0 || index >= length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
		}
		
		final T[] result = newArray((Class<T>) array.getClass().getComponentType(), length - 1);
		
		System.arraycopy(array, 0, result, 0, index);
		
		if (index < length - 1) {
			System.arraycopy(array, index + 1, result, index, length - index - 1);
		}
		
		return result;
	}
	
	public static <T> int getLength(final T[] array) {
		return array != null ? array.length : 0;
	}
	
	public static boolean isEmpty(final boolean[] array) {
		return array == null || array.length == 0;
	}
	
	public static boolean isEmpty(final byte[] array) {
		return array == null || array.length == 0;
	}
	
	public static boolean isEmpty(final int[] array) {
		return array == null || array.length == 0;
	}
	
	public static boolean isEmpty(final long[] array) {
		return array == null || array.length == 0;
	}
	
	public static boolean isEmpty(final float[] array) {
		return array == null || array.length == 0;
	}
	
	public static boolean isEmpty(final double[] array) {
		return array == null || array.length == 0;
	}
	
	public static <T> boolean isEmpty(final T[] array) {
		return array == null || array.length == 0;
	}
	
	public static <T> List<T> asModifiableList(final T[] array) {
		if (!isEmpty(array)) {
			final List<T> result = new ArrayList<>(array.length);
			
			Collections.addAll(result, array);
			
			return result;
		} else {
			return new ArrayList<T>();
		}
	}
	
	public static <T> T[] toArray(final Collection<T> collection, final Class<T> componentType) {
		if (collection != null) {
			return collection.toArray(newArray(componentType, collection.size()));
		} else {
			return null;
		}
	}
	
	public static <T extends Comparable<T>> T[] toArrayAndSort(
		final Collection<T> collection, final Class<T> componentType) {
		final T[] array = toArray(collection, componentType);
		
		if (!isEmpty(array)) {
			Arrays.sort(array);
		}
		
		return array;
	}
}

