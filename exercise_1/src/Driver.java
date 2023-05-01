public class Driver {

	public static void main(String[] args) {
		Buffer b = new Buffer();
		Sensor h = new Sensor(b, "Humidity", 0);
		Sensor l = new Sensor(b, "Light", 1);
		Sensor t = new Sensor(b, "Temperature", 2);
		WorkerDevice w = new WorkerDevice(b);
		h.start();
		l.start();
		t.start();
		w.start();
		// This is an endless program
	}

}
