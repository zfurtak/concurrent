import java.util.Random;

public class WorkerDevice extends Thread {
	private Random rnd = new Random();

	private Buffer b;

	public WorkerDevice(Buffer b) {
		this.b = b;
	}
	@Override
	public void run() {
		while(true) try {
			@SuppressWarnings("unused")
			int[] data = b.take();
			//System.out.println("WD has taken"+data[0]+" "+data[1]+" "+data[2]);
			sleep(rnd.nextInt(500)); // Represents the time used by the Worker to manage the data

		} catch (Exception x) { x.printStackTrace(); }
	}

}
