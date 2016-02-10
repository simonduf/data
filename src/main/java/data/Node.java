/**
 * 
 */
package data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The node is the main element for establishing connection between elements. It
 * contains some input and output is a placeholder. This class is abstract since
 * the connection logic between the input and the output needs to be implemented
 * otherwise the class is useless.
 * 
 * @author simon
 *
 */
public abstract class Node extends NamedElement {

	protected final List<Input<?>> inputs = new CopyOnWriteArrayList<Input<?>>();
	protected final List<Output<?>> outputs = new CopyOnWriteArrayList<Output<?>>();
	protected final List<NodeEventListener> listeners = new CopyOnWriteArrayList<NodeEventListener>();

	protected Node(String name) {
		super(name);
	}

	public List<Input<?>> getInputs() {
		return new ArrayList<Input<?>>(inputs);
	}

	public List<Output<?>> getOutputs() {
		return new ArrayList<Output<?>>(outputs);
	}

	/**
	 * Not implemented. This placeholder is meant to remove every reference to
	 * the node in order to help the garbage collector... TBD if needed
	 */
	public void dispose() {
		for(Input<?> i: getInputs())
		{
			if (i.getOutput() != null)
				i.getOutput().disconect(i);
		}
		
		for(Output<?> o: getOutputs())
		{
			o.disconectAll();
		}
		newEvent(new NodeEvent.DisposeEvent(this));
	}
	
	public boolean hasConnectedOutputs()
	{
		for(Output<?> o: getOutputs())
		{
			if(!o.getConnectedInputs().isEmpty())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean hasConnectedInputs()
	{
		for(Input<?> i: getInputs())
		{
			if(i.getOutput()!=null)
			{
				return true;
			}
		}
		return false;
	}
	
	public void addEventListener(NodeEventListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeEventListener(NodeEventListener listener)
	{
		listeners.remove(listener);
	}
	
	void newEvent(NodeEvent event)
	{
		for(NodeEventListener listener: listeners)
		{
			listener.newEvent(event);
		}
	}
	
	public interface NodeEventListener
	{
		public void newEvent(NodeEvent event);
	}
	

	
	
}
