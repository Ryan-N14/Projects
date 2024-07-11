public class Queue<T> implements QueueInterface<T> {
    T[] queueArray;
    private int head = 0;
    private int tail = 0;
    private int count = 0;
    private int max_count = 0;


    public Queue(){
        @SuppressWarnings("unchecked")
        T[] temp = (T[]) new Object[50];
        queueArray = temp;
    }


    @Override
    public void push(T value) {
        if((tail + 1) % 50 == head){
            //tail is one behind the head
            return;
        } else {
            //add to tail and moves tail index
            queueArray[tail] = value;
            tail = (tail + 1) % 50;
            count++;

            if(count > max_count){
                //counts become greater than most ever in queue
                max_count = count;
            }
        }
    }

    @Override
    public T pop() {
        if(head == tail){
            //Nothing left in queue
            return null;
        } else {
            T temp = queueArray[head];
            //moves head to next element
            head = (head + 1) % 50;
            count--;
            return temp;
        }
    }

    @Override
    public T peek() {
        if(isEmpty()){
            return null;
        } else {
            return queueArray[head];
        }
    }

    @Override
    public boolean isEmpty() {
        if(head == tail){
            return true;
        }
        return false;
    }

    public String toString(){
        String valuesInQueue = "";
        //Put all values that are inside circular array into string. 
        for (T t : queueArray) {
            valuesInQueue += t + ", " ;
        }

        return valuesInQueue;
    }

    @Override
    public int max_count() {
        return max_count;
    }
}

