package Exercise03;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Runway {
	// This object is a utility to be used by the Threads
	public static final Random rnd = new Random();
	private final Semaphore[] workFromDir = new Semaphore[CardinalDirections.values().length];
	private final Semaphore useRunway = new Semaphore(1, true);
	Semaphore queueOfPlanes = new Semaphore(1, true);
	Semaphore waitForPermission = new Semaphore(0, true);
	Semaphore landingFinished = new Semaphore(0, true);


	public Runway(){
		for(int i=0; i < CardinalDirections.values().length; i++){
			workFromDir[i] = new Semaphore(0, true);
		}
	}

	public void requestLanding(int id, CardinalDirections aDir) throws InterruptedException {
		System.out.println("Plane "+id+" requires land from "+aDir);
		queueOfPlanes.acquire();
		workFromDir[aDir.ordinal()].release();
		// wait for the controller to notify, that the plane can land
		System.out.println("Plane "+id+" waits for permission to land from "+aDir);
		waitForPermission.acquire();
		queueOfPlanes.release();
		System.out.println("Plane "+id+" is landing from "+aDir);

	}

	public void finishLanding(int id, CardinalDirections aDir) {
		System.out.println("Plane "+id+" finishes landing from "+aDir);
		landingFinished.release();
	}

	public void listenForLanding(CardinalDirections aDir) throws InterruptedException {
		workFromDir[aDir.ordinal()].acquire();
		useRunway.acquire();
		// Here the runway is available
		// we have to notify the plane
		waitForPermission.release();
		landingFinished.acquire();
		useRunway.release();
	}

}
