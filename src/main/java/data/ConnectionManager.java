/**
 * 
 */
package data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import data.Node.NodeEventListener;
import data.NodeEvent.DisconnectEvent;
import data.exceptions.InvalidDataTypeException;

/**
 * This class is used to connect the element and keep a reference to them in
 * order to display them pater on.
 * 
 * @author simon
 *
 */
public class ConnectionManager implements NodeEventListener
{
	private ConnectionManager()
	{
	}
	
	private final static ConnectionManager	cm	= new ConnectionManager() {
												};
	
	public static ConnectionManager getInstance()
	{
		return cm;
	}
	
	private final List<Node>	nodes		= new CopyOnWriteArrayList<Node>();
	private final List<Node>	keepNode	= new CopyOnWriteArrayList<Node>();
	
	/**
	 * Connect the provided input with the output
	 * 
	 * @param i
	 *            the input
	 * @param o
	 *            the output
	 * @throws InvalidDataTypeException
	 *             when the type expected by the input does not match with the
	 *             one provided by the output
	 */
	public void connect(Input<?> i, Output<?> o) throws InvalidDataTypeException
	{
		add(i.getParent());
		add(o.parent);
		
		o.connect((Input<?>) i);
	}
	
	/**
	 * Add a Node to the list of existing nodes. This may be useful for object
	 * not connected that we want to display on the screen.
	 * 
	 * @param n
	 *            the node to add
	 */
	public void add(Node n)
	{
		if (!nodes.contains(n))
		{
			nodes.add(n);
		}
		n.addEventListener(this);
	}
	
	/**
	 * @return a copy of the list of nodes.
	 */
	public List<Node> getNodes()
	{
		return new ArrayList<Node>(nodes);
	}
	
	/**
	 * Disconnect everything from the node and destroy every reference to the
	 * node. 
	 * 
	 * @param node to be destroyed
	 */
	public void dispose(Node node)
	{
		node.removeEventListener(this);
		node.dispose();
		nodes.remove(node);
		keepNode.remove(node);
	}
	
	/**
	 * The connection manager has a list of node to not cascade the destruction of...
	 * @param node is the node to keep when cascading disconections
	 */
	public void keep(Node node)
	{
		keepNode.add(node);
	}
	
	public List<Node> getkeeplist()
	{
		return new ArrayList<Node>(keepNode);
	}
	
	
	/* (non-Javadoc)
	 * @see data.Node.NodeEventListener#newEvent(data.NodeEvent)
	 * This is to listen do other node disconnection.
	 */
	@Override
	public void newEvent(NodeEvent event)
	{
		if (event instanceof DisconnectEvent)
		{
			if (keepNode.contains(event.getSource()))
				return;
			
			if (!event.getSource().hasConnectedOutputs() || !event.getSource().hasConnectedInputs())
			{
				dispose(event.getSource());
			}
		}
	}
	
}
