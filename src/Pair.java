public class Pair<T, K> {
    private T first;
    private K second;

    public Pair() {
        this.first = null;
        this.second = null;
    }

    public Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

    public boolean isEmpty() {
        return (first == null && second == null);
    }

    public T getFirst() {
        return first;
    }

    public K getSecond() {
        return second;
    }
}
