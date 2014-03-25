package requestCounter;

import java.util.Iterator;

public class ArrayCircularBuffer<T> implements ICircularBuffer<T> {

	private final T[] arr;
	private final int size;
	private int head = 0;
	private int tail = -1;
	private int length = 0;
	
	@SuppressWarnings("unchecked")
	public ArrayCircularBuffer(int size){
		this.size = size;
		arr = (T[]) new Object[size];
	}
	
	@Override
	public void add(T t) {
		tail = (tail == size - 1) ? 0 : tail + 1;
		arr[tail] = t;
		if(length == size){
			head = (head == size - 1) ? 0 : head + 1;
		}else{
			length++;
		}
	}

	
	private class CricularIterator<T> implements Iterator<T>{
		private int currentPoint = tail;
		private int currentLength = 0;
		@Override
		public boolean hasNext() {
			if(currentLength == length){
				return false;
			}
			return true;
		}

		@Override
		public T next() {
			@SuppressWarnings("unchecked")
			T obj = (T) arr[currentPoint];
			currentPoint = (currentPoint == 0) ? size - 1 : currentPoint - 1;
			currentLength++;
			return obj;
		}

		@Override
		public void remove() {
			
		}}
	
	@Override
	public Iterator<T> iteratorFromTailToHead() {
		return new CricularIterator<T>();
	}

}
