/**
 * 
 */
package data.annotations;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Simon Dufour
 *
 */
public class ReflexionConnectionManager {
	private final List<Object>	objects		= new CopyOnWriteArrayList<>();
	
	
	public void add(Object o)
	{
		Node n = o.getClass().getAnnotation(Node.class);
		if(n!=null)
			objects.add(o);
	}
	
	public void connect( Object inputNode, String inputName, Object outputNode, String outputName )
	{
		if(!isNode(inputNode))
			throw new RuntimeException();
		if(!isNode(outputNode))
			throw new RuntimeException();
	}

	public boolean isNode(Object o)
	{
		return o.getClass().getAnnotation(Node.class)!= null;
	}
	
	public String getName(Object o )
	{
		if(isNode(o))
			return (o.getClass().getAnnotation(Node.class)).name();
		return null;
	}
	
//	public Method getInput(Object o, String inputName, Class<?> outputType)
//	{
//		 for (Method method : o.getClass().getMethods()) {
//	            Class<?>[] parameterTypes = method.getParameterTypes();
//	            if (method.getAnnotation(Input.class) == null || parameterTypes.length != 1) continue;
//	            Class<?> inputType = parameterTypes[0];
//	            if(inputType.isAssignableFrom(outputType))
//	            	return method;
//	        }
//		return null;
//	}
	
	public Output<?> getOutputByName(Object o, String outputName)
	{
		if(outputName == null || outputName.isEmpty())
			throw new RuntimeException("Cannot search for non existing or empty name");
		for ( Field field : o.getClass().getFields())
			if(Output.class.isAssignableFrom(field.getType()))
				if(outputName.equals(field.getName()))
					try {
						return (Output<?>) field.get(o);
					} catch (IllegalAccessException | IllegalArgumentException e) {
						e.printStackTrace();
					}
		
		return null;
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
	
}
