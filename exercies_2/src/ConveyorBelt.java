import java.util.Random;
import java.util.concurrent.Semaphore;

public class ConveyorBelt {

    private final int size;
    // This object is a utility to be used by the Threads
    public static final Random rnd = new Random();

    private final Semaphore canPut;
    private final Semaphore[] canTake;
    private final Semaphore mutex;

    private int[] data;
    private int[] type;

    public ConveyorBelt(int size) {
        data = new int[size];
        type = new int[size];
        this.size = size;

        canPut = new Semaphore(size, true);
        canTake = new Semaphore[3];
        for(int i = 0; i < 3; i++)
            canTake[i] = new Semaphore(0, true);

        this.mutex = new Semaphore(1, true);

        for (int i = 0; i < size; i++){
            type[i] = 0;
            data[i] = -1;
        }
    }

    public void put(int type, int item) throws InterruptedException {
        // musimy być pewni, czy jest jeszcze miejsce w buforze
        // jeśli tak to je zajmujemy i wstawiamy na pierwsze wolne miejsce produkt
        canPut.acquire();
        // do we need this mutex here???
        mutex.acquire();
        int i = 0;
        while (this.type[i] != 0) i++;
        this.type[i] = type;
        this.data[i] = item;
        System.out.println("PUTTING type: "+type);
        // udostępniamy informacje dla konkretnego robota
        // ze jest nowy element do zabrania
        canTake[type-1].release();
        mutex.release();
    }

    // In take, the Consumer id has to wait if there is no item of type id
    public int take(int id) throws InterruptedException {
        // bierzemy przedmiot z danego typu, więc ujmujemy z semafora
        canTake[id-1].acquire();
        // upewniamy się ze jeden robot-taker pracuje naraz
        mutex.acquire();
        int i = 0;
        while (type[i] != id) i++;
        type[i] = 0;
        data[i] = -1;
        System.out.println("TAKING type: "+id);
        // dajemy info dla robota-puttera że jest nowe miejsce
        canPut.release();
        mutex.release();
        return 0;
    }

}
