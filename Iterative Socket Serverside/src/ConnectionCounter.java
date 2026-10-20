import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionCounter {
    private AtomicInteger counter = new AtomicInteger();

    public void clientConnect(){
        counter.incrementAndGet();
    }

    public void clientDisconect(){
        counter.decrementAndGet();
    }

    public int getActive(){
        return counter.get();
    }
}
