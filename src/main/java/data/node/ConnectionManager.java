/**
 * 
 */
package data.node;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Simon Dufour
 *
 */
public class ConnectionManager {
	private final List<Node>	nodes		= new CopyOnWriteArrayList<>();
	
	
	public void add(Node o)
	{
		nodes.add(o);
	}
	
	public List<Node> getNodes()
	{
		return new ArrayList<Node>(nodes);
	}
	
	public void dispose(Node n)
	{
		nodes.remove(n);
		
		//TODO
		//n.getOutputs.disconnect...
		//n.getInputs.disconnect
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
	public  void connect(Input<?> i, Output<?> o)
	{
		if(!isConnectable(i,o))
			throw new RuntimeException();
		
		((Output<Object>)o).connect( (Input<Object>)i );
	}
	
	
	@SuppressWarnings("unchecked")
	public void disconnect(Input<?> i, Output<?> o)
	{	
		((Output<Object>)o).disconect( (Input<Object>)i );
		//TODO event?
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void connect( Node inputNode, String inputName, Node outputNode, String outputName )
	{
		if(!isNode(inputNode))
			throw new RuntimeException();
		if(!isNode(outputNode))
			throw new RuntimeException();
		
		Output output = getOutputByName(outputNode, outputName);
		Input input = getInputByName(inputNode, inputName);
		
		if( output==null || input == null)
			throw new RuntimeException();
		//TODO exceptions
		
		connect(input, output);
	}

	public boolean isNode(Object o)
	{
		return o instanceof Node;
		//return o.getClass().getAnnotation(Node.class)!= null;
	}
	
	@SuppressWarnings("unchecked")
	private <T> Map<String,T> getFieldFromType(Object o, Class<T> c)
	{
		Map<String,T> results= new HashMap<>();
		for ( Field field : o.getClass().getFields())
			if(c.isAssignableFrom(field.getType()))
					try {
						results.put(field.getName(), (T) field.get(o)); 
					} catch (IllegalAccessException | IllegalArgumentException e) {
						e.printStackTrace();
					}
		
		return results;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Map<String, Output> getOutputs(Node n)
	{
		Map<String, Output> map =  getFieldFromType(n, Output.class);
		if(n.getOutputs()!=null)
			n.getOutputs().forEach(map::putIfAbsent);
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	public Map<String, Input> getInputs(Node n)
	{
		Map<String, Input> map = getFieldFromType(n, Input.class);
		if(n.getInputs()!=null)
			n.getInputs().forEach(map::putIfAbsent);
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
}
