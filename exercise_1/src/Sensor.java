import java.util.Random;

public class Sensor extends Thread {
	private static Random rnd = new Random();
	private Buffer b;
	private String name;
	private int id;

	public Sensor(Buffer b, String name, int i) {
		this.b = b;
		this.name = name;
		this.id = i;
	}
	@Override
	public void run() {
		while(true) try {
			sleep(rnd.nextInt(500)); // We simulate the time needed to take a sample
			int sample = rnd.nextInt(10);
			b.put(id, name, sample);
		} catch (Exception x) { x.printStackTrace(); }
	}

}
