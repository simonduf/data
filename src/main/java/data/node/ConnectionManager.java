/**
 * 
 */
package data.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import data.node.Disposable.DisposableEventListener;
import data.node.Disposable.DisposableEventProvider;

/**
 * @author Simon Dufour
 *
 */
public class ConnectionManager implements DisposableEventListener {
	private final List<Node>	nodes		= new CopyOnWriteArrayList<>();
	private final List<NodeEventListener>	listeners		= new CopyOnWriteArrayList<>();
	
	public void add(Node n)
	{
		if(nodes.contains(n))
			return;
		
		if(n instanceof DisposableEventProvider)
			((DisposableEventProvider) n).addDisposeListener(this);
		
		nodes.add(n);
		sendEvent(new NodeEvent(NodeEvent.NEW_NODE, n, null, null, null ) );
	}
	
	public List<Node> getNodes()
	{
		return new ArrayList<Node>(nodes);
	}
	
	@Override
	public void wasDisposed(Object o) 
	{
		if(! (o instanceof Node))
			return;
		
		Node n = (Node)o;
		
		for(Output<?> output: getOutputs(n).values())
			for(Input<?> i: output.getConnectedInputs())
				disconnect( i, output );
		
		//TODO Test this
		//Disconnect any input of the referenced node
		for(Node nodeInList : nodes)
			for(Output<?> outputInList: getOutputs(nodeInList).values() )
				for(Input<?> inputInList : outputInList.getConnectedInputs() )
					if(getInputs(n).containsValue(inputInList))
						disconnect(inputInList, outputInList);
		
		nodes.remove(n);
		
		sendEvent(new NodeEvent(NodeEvent.DISPOSE_NODE, n, null, null, null ) );
	}
	
	public void dispose(Node n)
	{
		// If the node is disposable, we should have register the ConnectionManager to the DisposeEventProvider
		// The callback from calling dispose should call wasDisposed when the object is ready.
		if(n instanceof Disposable)
			((Disposable) n).dispose();
		
		// We call the method manually because there is no harm calling it twice for disposable...
		// Also call it for node that are not disposable
		wasDisposed(n);
	}
	
	public boolean isConnectable(Input<?> i, Output<?> o)
	{
		Node parentI = getParent(i);
		Node parentO = getParent(o);		
		if( parentI != null && parentI == parentO )
			return false;
		
		return getOutputType(o) == getInputType(i);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void connect(Input<T> i, Output<T> o)
	{
		if(!isConnectable(i,o))
			throw new RuntimeException();
		
		((Output<Object>)o).connect( (Input<Object>)i );
		sendEvent(new NodeEvent(NodeEvent.CONNNECTION, getParent(o), getParent(i),i,o ) );
	}
	
	
	@SuppressWarnings("unchecked")
	public void disconnect(Input<?> i, Output<?> o)
	{	
		((Output<Object>)o).disconect( (Input<Object>)i );
		 sendEvent(new NodeEvent(NodeEvent.DISCONNECTION, getParent(o), getParent(i),i,o ) );
	}
	
	@SuppressWarnings("unchecked")
	public void connect( Node inputNode, String inputName, Node outputNode, String outputName )
	{
		if(!isNode(inputNode))
			throw new RuntimeException();
		if(!isNode(outputNode))
			throw new RuntimeException();
		
		Output<?> output = getOutputByName(outputNode, outputName);
		Input<?> input = getInputByName(inputNode, inputName);
		
		if( output==null || input == null)
			throw new RuntimeException();
		//TODO exceptions
		
		connect((Input<Object>) input,(Output<Object>) output);
	}

	public boolean isNode(Object o)
	{
		return o instanceof Node;
		//return o.getClass().getAnnotation(Node.class)!= null;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Map<String, Output> getOutputs(Node n)
	{
		Map<String, Output> map =  ReflectionUtils.getFieldFromType(n, Output.class);
		Map<String, Output<?>> dynamicOutputs = n.getDynamicOutputs();
		if(dynamicOutputs!=null)
			dynamicOutputs.forEach(map::putIfAbsent);
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	public Map<String, Input> getInputs(Node n)
	{
		Map<String, Input> map = ReflectionUtils.getFieldFromType(n, Input.class);
		Map<String, Input<?>> dynamicInput = n.getDynamicInputs();
		if(dynamicInput !=null)
			dynamicInput.forEach(map::putIfAbsent);
		return map;
	}
	
	public Output<?> getOutputByName(Node n, String outputName)
	{
		if(outputName == null || outputName.isEmpty())
			throw new RuntimeException("Cannot search for non existing or empty name");		
		return getOutputs(n).get(outputName);
	}
	
	public Input<?> getInputByName(Node n, String outputName)
	{
		if(outputName == null || outputName.isEmpty())
			throw new RuntimeException("Cannot search for non existing or empty name");		
		return getInputs(n).get(outputName);
	}
	
	
	public Class<?> getOutputType(Output<?> output)
	{
		List<Class<?>> result = ReflectionUtils.getTypeArguments(Output.class, output.getClass());
		if(result.size()>1)
			System.out.println("Output has multiple generic type argument? This was never tested. \n" + result );
		return result.get(0);
	}
	
	public Class<?> getInputType(Input<?> input)
	{
		List<Class<?>> result = ReflectionUtils.getTypeArguments(Input.class, input.getClass());
		if(result.size()>1)
			System.out.println("Output has multiple generic type argument? This was never tested. \n" + result );
		return result.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Output<T> getOutput(Input<T> input)
	{
		for(Node n: nodes)
			for(Output<T> o:getOutputs(n).values())
				if(o.getConnectedInputs().contains(input))
					return o;
		return null;
	}
	
	public String getName(Input<?> i)
	{
		return getInputs(getParent(i)).entrySet().stream()
				  .filter(entry -> Objects.equals(entry.getValue(), i))
	              .map(Map.Entry::getKey)
	              .findAny().get();
	}
	
	public String getName(Output<?> i)
	{
		return getOutputs(getParent(i)).entrySet().stream()
				  .filter(entry -> Objects.equals(entry.getValue(), i))
	              .map(Map.Entry::getKey)
	              .findAny().get();
	}
	
	public Node getParent(Input<?> input)
	{
		for(Node n: nodes)
			for(Input<?> i : getInputs(n).values())
				if(i == input)
					return n;
		return null;
	}
	
	public Node getParent(Output<?> output)
	{
		for(Node n: nodes)
			for(Output<?> o:getOutputs(n).values())
				if(o == output)
					return n;
		return null;
	}
	
	public static interface NodeEventListener extends Consumer<NodeEvent>{};
	
	public static class NodeEvent
	{
		public final static String CONNNECTION = "Connection";
		public final static String DISCONNECTION = "Disconnection";
		public final static String NEW_NODE = "New Node";
		public final static String DISPOSE_NODE = "Dispose";
		
		public final String event;
		public final Node sourceNode;
		public final Node destNode;
		public final Input<?> destInput;
		public final Output<?> sourceOutput;
		
		public NodeEvent(String event, Node sourceNode, Node destNode, Input<?> destInput, Output<?> sourceOutput) {
			super();
			this.event = event;
			this.sourceNode = sourceNode;
			this.destNode = destNode;
			this.destInput = destInput;
			this.sourceOutput = sourceOutput;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((destInput == null) ? 0 : destInput.hashCode());
			result = prime * result + ((destNode == null) ? 0 : destNode.hashCode());
			result = prime * result + ((event == null) ? 0 : event.hashCode());
			result = prime * result + ((sourceNode == null) ? 0 : sourceNode.hashCode());
			result = prime * result + ((sourceOutput == null) ? 0 : sourceOutput.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NodeEvent other = (NodeEvent) obj;
			if (destInput == null) {
				if (other.destInput != null)
					return false;
			} else if (!destInput.equals(other.destInput))
				return false;
			if (destNode == null) {
				if (other.destNode != null)
					return false;
			} else if (!destNode.equals(other.destNode))
				return false;
			if (event == null) {
				if (other.event != null)
					return false;
			} else if (!event.equals(other.event))
				return false;
			if (sourceNode == null) {
				if (other.sourceNode != null)
					return false;
			} else if (!sourceNode.equals(other.sourceNode))
				return false;
			if (sourceOutput == null) {
				if (other.sourceOutput != null)
					return false;
			} else if (!sourceOutput.equals(other.sourceOutput))
				return false;
			return true;
		}
		
		
	}
	
	protected void sendEvent(NodeEvent e )
	{
		for(NodeEventListener l : listeners)
			l.accept(e);
	}
	
	public void addListener( NodeEventListener listener )
	{
		listeners.add(listener);
	}
	
	public void removeListener( NodeEventListener listener )
	{
		listeners.remove(listener);
	}
}
