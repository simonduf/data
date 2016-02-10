/**
 * 
 */
package data;

import data.exceptions.InvalidNodeOperationException;

/**
 * @author sdufour
 *
 */
public abstract class Input<T> extends NamedElement
{
	private final Node parent;
	private final Class<?> inputType;
	private Output<T> output = null;
	
	protected Input(String name,Class<?> inputType, Node parent )
	{
		super(name);
		this.inputType = inputType;
		this.parent = parent;
	}

	public abstract void newData(T data);
	
	public Node getParent()
	{
		return parent;
	}
	
	public Class<?> getInputType()
	{
		return inputType;
	}
	
	Output<T> getOutput()
	{
		return output;
	}
	
	@SuppressWarnings("unchecked")
	void connectOutput(Output<?> output)
	{
		if(this.output != null && output != null)
			throw new InvalidNodeOperationException("output already connected");
		this.output = (Output<T>) output;
		parent.newEvent(new NodeEvent.ConnectEvent(parent));
	}
	
	void disconect(Output<?> output)
	{
		if(this.output != output)
			throw new InvalidNodeOperationException("Tring to disconect an output that is not connected to this");
		this.output = null;
		parent.newEvent(new NodeEvent.DisconnectEvent(parent));
	}
}
