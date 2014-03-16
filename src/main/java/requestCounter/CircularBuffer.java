package requestCounter;

import java.util.Iterator;
import java.util.LinkedList;

public class CircularBuffer<T> implements ICircularBuffer<T> {

	private LinkedList<T> list = new LinkedList<T>();;
	private final int maximalSize;
	
	public CircularBuffer(int maximalSize){
		this.maximalSize = maximalSize;
	}
	
	public CircularBuffer(){
		this(60);
	}
	
	public void add(T t) {
		if(list.size() < maximalSize){
			list.add(t);
		}else{
			list.removeFirst();
			list.add(t);
		}
	}

	public Iterator<T> iteratorFromTailToHead() {
		return list.descendingIterator();
	}

}
