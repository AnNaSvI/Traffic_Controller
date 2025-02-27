public class TrafficControllerSimple implements TrafficController {
    private final TrafficRegistrar registrar;
    private boolean empty = true;

    public TrafficControllerSimple(TrafficRegistrar registrar) {
        this.registrar = registrar;
    }

    
    public synchronized void enterRight(Vehicle v) {
        try {
            
            while (!empty) {
                wait();
            }
            empty = false;
            registrar.registerRight(v);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }
    }

    public synchronized void enterLeft(Vehicle v) {
        try {
            while (!empty) {
                wait();
            }
            empty = false;
            registrar.registerLeft(v);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }
    }

    public synchronized void leaveRight(Vehicle v) {
        empty = true;
        registrar.deregisterRight(v);
        notify();  
    }

    public synchronized void leaveLeft(Vehicle v) {
        empty = true;
        registrar.deregisterLeft(v);
        notify();  
    }
}
