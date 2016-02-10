/**
 * 
 */
package data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import data.exceptions.InvalidDataTypeException;
import data.exceptions.InvalidNodeOperationException;

/**
 * @author sdufour
 *
 */
public class Output<T> extends NamedElement {

	final Node parent;
	private final List<Input<T>> inputs = new CopyOnWriteArrayList<Input<T>>();
	private final Class<?> outputType;

	/**
	 * Since generic type information is removed from the runtime, the class
	 * need a way to dynamically know what can be connected. To achieve this,
	 * the class is transfered to the output instance and save.
	 * 
	 * @param name
	 *            is the name of the node
	 * @param outputType
	 *            is the class accepted by the output.
	 * @param parent
	 *            the parent node!
	 */
	public Output(String name, Class<?> outputType, Node parent) {
		super(name);
		this.outputType = outputType;
		this.parent = parent;
	}

	/**
	 * @return the parent node of this output
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Add a listener to this output. The output can have as many listener as
	 * the ram allow but it will reject the input if the data type is not the same.
	 * Throw a RuntimeException if the listener is already in the list.
	 * 
	 * @param input
	 * @throws InvalidDataTypeException 
	 */
	@SuppressWarnings("unchecked")
	public void connect(Input<?> input) throws InvalidDataTypeException {
		if (inputs.contains(input))
			throw new InvalidNodeOperationException("Listener already in the list" + input.toString());

		if (!isInputConnectable(input)) {
			throw new InvalidDataTypeException("Not legal connections : " + input.toString() + "(expected:" + input.getInputType().getName() + " provided : " + outputType.getName());
		}
		input.connectOutput(this);
		inputs.add((Input<T>) input);
		parent.newEvent(new NodeEvent.ConnectEvent(parent));
	}

	/**
	 * Remove the listener from the output.
	 * Throw a runtimeException if the listener is not in the list.
	 * @param dataListener is the listener to be removed
	 */
	public void disconect(Input<?> input) {
		if (!inputs.remove(input)) {
			throw new InvalidNodeOperationException("Listener not in the list" + input.toString());
		}
		input.disconect(this);
		parent.newEvent(new NodeEvent.DisconnectEvent(parent));
	}

	public List<Input<T>> getConnectedInputs() {
		return new ArrayList<Input<T>>(inputs);
	}

	public boolean isInputConnectable(Input<?> input) {
		// TODO: maybe use the other way arround...
		return outputType.isAssignableFrom(input.getInputType());
	}

	
	//TODO change to package?
	//TODO catch exceptions?
	/**
	 * Method used by the node to send data to the output.
	 * @param data to send
	 */
	public void send(T data) {
		for (Input<T> o : inputs) {
			o.newData(data);
		}

	}

	public void disconectAll() {
		for(Input<T> i : getConnectedInputs() )
		{
			disconect( i);
		}
	}
}
