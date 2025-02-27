import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

public class TrafficControllerFair implements TrafficController {
    private final TrafficRegistrar registrar;
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition emptyCondition = lock.newCondition();
    private boolean empty = true;

    public TrafficControllerFair(TrafficRegistrar registrar) {
        this.registrar = registrar;
    }

   private void enter(Vehicle v, BiConsumer<TrafficRegistrar, Vehicle> registrarAction) {
    lock.lock();
    try {
        while (!empty) {
            try {
                emptyCondition.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                return; 
            }
        } 
        empty = false;
    } finally {
        lock.unlock(); 
    }
    
    registrarAction.accept(registrar, v);
	}


    private void leave(Vehicle v, BiConsumer<TrafficRegistrar, Vehicle> registrarAction) {
        try {
            lock.lock();
            empty = true;
            registrarAction.accept(registrar, v);
            emptyCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }



    @Override
    public void enterLeft(Vehicle v) {
        enter(v, TrafficRegistrar::registerLeft);
    }

    @Override
    public void enterRight(Vehicle v) {
        enter(v, TrafficRegistrar::registerRight);
    }

    @Override
    public void leaveLeft(Vehicle v) {
        leave(v, TrafficRegistrar::deregisterLeft);
    }

    @Override
    public void leaveRight(Vehicle v) {
        leave(v, TrafficRegistrar::deregisterRight);
    }
}
