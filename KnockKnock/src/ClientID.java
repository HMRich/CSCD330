
public class ClientID {

	private int counter;

	public ClientID() {
		counter = 0;
	}

	public int assignID() {
		if (counter == 500000)
			counter = 0;
		return ++counter;
	}

}
