import java.util.HashMap;

public class ThreadManager extends Thread {

	private HashMap<Integer, JokeThread> mActiveThreads;

	public ThreadManager() {
		mActiveThreads = new HashMap<Integer, JokeThread>();
		this.start();
	}
	
	public synchronized void addJokeThread(JokeJob jokeJob) {
		JokeThread thread = new JokeThread(jokeJob);
		mActiveThreads.put(jokeJob.getClientId(), thread);
	}

	public synchronized JokeThread getJokeThread(int key) {
		if (mActiveThreads.containsKey(key)) {
			return mActiveThreads.get(key);
		}
		return null;
	}

	public synchronized void cleanDeadThreads() {
		mActiveThreads.forEach((integer, jokeThread) -> {
			if (!jokeThread.isAlive()) {
				mActiveThreads.remove(integer);
			}
		});
	}


	
	
	public void run() {
		do {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException sleepingThread) {
				sleepingThread.printStackTrace();
			}
			cleanDeadThreads();
		} while(true);
	}

}