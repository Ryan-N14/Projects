public class Stack<T> implements QueueInterface<T>{
    T[] arrayStack;
    private int top = 0;
    private int count = 0;
    private int max_count = 0;
    
    public Stack(){
        T[] temp = (T[]) new Object[50];
        arrayStack = temp;
    }

    @Override
    public void push(T value) {
        if(count == 50){
            //array is full
            return;
        } else {
            arrayStack[top] = value;
            top++;
            count++;

            if(count > max_count){
                max_count = count;
            }
        }
       
    }

    @Override
    public T pop() {
        if(isEmpty()){
            return null;
        } else {
            T temp = arrayStack[top - 1];
            top--;
            count--;
            return temp;
        }
    }

    @Override
    public T peek() {
        if(isEmpty()){
            return null;
        } else{
            return arrayStack[top - 1];
        }
    }

    @Override
    public boolean isEmpty() {
        if(count == 0){
            return true;
        }
        return false;
    }

    public String toString(){
        String newString = "";

        for (T t : arrayStack) {
            newString += t + ", ";
        }

        return newString;
    }

    @Override
    public int max_count() {
        return max_count;
    }
}
