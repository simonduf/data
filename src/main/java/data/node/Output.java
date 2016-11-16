/**
 * 
 */
package data.node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import data.exceptions.InvalidDataTypeException;
import data.exceptions.InvalidNodeOperationException;

/**
 * Class containing the list of connected inputs
 * 
 * This class need to be extended to provide runtime Type Safety because of type
 * erasure for generic types.
 * 
 * @author sdufour
 *
 */
public abstract class Output<T> {

	private final List<Consumer<T>> inputs = new CopyOnWriteArrayList<>();

	/**
	 * Add a listener to this output. The output can have as many listener.
	 * Throw a RuntimeException if the listener is already in the list.
	 * 
	 * @param input the input to connect
	 * @throws InvalidDataTypeException if data type does not match 
	 */
	public void connect(Consumer<T> input) throws InvalidDataTypeException {
		if (inputs.contains(input))
			throw new InvalidNodeOperationException("Listener already in the list" + input.toString());

		inputs.add(input);
	}

	/**
	 * Remove the listener from the output.
	 * Throw a runtimeException if the listener is not in the list.
	 * @param input is the listener to be removed
	 */
	public void disconect(Consumer<T> input) {
		if (!inputs.remove(input)) {
			throw new InvalidNodeOperationException("Listener not in the list" + input.toString());
		}
	}

	public List<Consumer<T>> getConnectedInputs() {
		return new ArrayList<Consumer<T>>(inputs);
	}

	//TODO catch exceptions?
	/**
	 * Method used by the node to send data to the output.
	 * @param data to send
	 */
	public void send(T data) {
		for (Consumer<T> i : inputs) {
			i.accept(data);
		}

	}
}
