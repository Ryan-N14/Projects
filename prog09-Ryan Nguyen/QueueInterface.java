/** Defines the methods for any type of Queue - FIFO, LIFO, priority */
public interface QueueInterface<T> {

    /**
     * add value to queue
     * @param value
     */
    public void push(T value);

    /**
     * removes value from queue
     * @return
     */
    public T pop();

    /**
     * look at next value on queue
     * @return
     */
    public T peek();

    /**
     * checks if array is empty
     * @return
     */
    public boolean isEmpty();

    /**
     * max count of queue 
     * @return
     */
    public int max_count();
}