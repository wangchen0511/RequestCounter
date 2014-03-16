package requestCounter;

import java.util.Iterator;

public interface ICircularBuffer<T> {
	public void add(T t);
	
	public Iterator<T> iteratorFromTailToHead();
}
