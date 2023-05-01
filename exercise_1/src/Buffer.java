import java.util.concurrent.Semaphore;

public class Buffer {

    private final int NUMBER_OF_SAMPLES = 3;
    private int[] samples = new int[3];
    private final Semaphore[] readyToPut = new Semaphore[NUMBER_OF_SAMPLES];
    private final Semaphore[] readyToTake = new Semaphore[NUMBER_OF_SAMPLES];
    public Buffer(){
        for(int i = 0; i < NUMBER_OF_SAMPLES; i++){
            readyToPut[i] = new Semaphore(1, true);
            readyToTake[i] = new Semaphore(0, true);
        }
    }

    public int[] take() throws InterruptedException {
        //jeśli może wziąć (sensor zreasował) to bierze
        // musi poczekać na wszystkie próbki
        for(int i=0; i < NUMBER_OF_SAMPLES; i++){
            readyToTake[i].acquire();
        }
        System.out.println("Worker device is taking samples");
        int[] ret = samples.clone();

        // po skończeniu pracy, daje pozwolenie na dostarczanie spowrotem sampli
        for(int i=0; i < NUMBER_OF_SAMPLES; i++){
            readyToPut[i].release();
        }
        return ret;
    }

    public void put(int id, String name, int sample) throws InterruptedException {
        // sensor dodaje sample, jak może, czyt. jak device skończy pracę
        // początkowo może bo tablica jest pusta, semafory ustawione na 1
        readyToPut[id].acquire();
        samples[id] = sample;
        System.out.println("Sensor: "+id+" has put samples: "+sample);

        // po dodaniu sampla umożliwia workerowi zabranie próbki
        readyToTake[id].release();
    }
}
