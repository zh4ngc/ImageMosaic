package com.utils;

public class CircularIntDoubleArrayIterator {
	private int[][] arr;
	private int cursor;

	public CircularIntDoubleArrayIterator(int[][] doubleIntArray)
			throws IllegalArgumentException {
		if (doubleIntArray.length == 0) {
			throw new IllegalArgumentException("Int array is size 0.");
		}

		arr = doubleIntArray;
		cursor = -1;
	}

	public int[] nextIntArray() {
		cursor++;

		if (cursor >= arr.length) {
			cursor = 0;
		}
		
		return arr[cursor];
	}

	public int getNextValidCursorPos() {
		if (cursor < 0) {
			if (arr.length > 1) {
				return 1;
			} else {
				return 0;
			}
		}

		int nextPos = cursor + 1;

		if (nextPos >= arr.length) {
			nextPos = 0;
		}

		return nextPos;
	}

	public int getCurrentCursorPos() {
		return (cursor < 0 ? 0 : cursor);
	}
	
	public int getRandomCursorPos() {
		cursor = (int) (Math.random() * arr.length);
		return cursor;
	}
	
}
