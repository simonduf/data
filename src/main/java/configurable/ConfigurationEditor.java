/**
 * 
 */
package configurable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Simon Dufour
 *
 */
public class ConfigurationEditor {
	public final List<Parameter> parameters = new ArrayList<>();
	public final Object object;
	
	public ConfigurationEditor(Object o)
	{
		this.object = o;
		List<Method> getters = getMethodsAnnotatedWith( o.getClass() , Getter.class);
		List<Method> setters = getMethodsAnnotatedWith( o.getClass() , Setter.class);
		
		for( Method getter: getters )
		{
			for( Method otherGetter: getters )
			{
				if(getter == otherGetter)
					continue;
				if(getName(getter).equals(getName(otherGetter)))
					throw new IllegalArgumentException("An object cannot have two getter with the same Name");
			}
		}
		
		for( Method setter: setters )
		{
			for( Method otherSetter: setters )
			{
				if(setter == otherSetter)
					continue;
				if(getName(setter).equals(getName(otherSetter)))
					throw new IllegalArgumentException("An object cannot have two setter with the same Name");
			}
		}
		
		//TODO handle setter methods with not getter?
		for( Method getter: getters )
		{
			String name = getName(getter);
			for( Method setter: setters )
			{
				if(name.equals(getName(setter)))
					parameters.add( new Parameter(name, o, getter, setter) );
			}
		}
	}
	
	public static String getName( Method m)
	{
		Getter getter = m.getAnnotation( Getter.class);
		if(getter!=null)
			return getter.name();
		
		Setter setter = m.getAnnotation( Setter.class);
		if(setter!=null)
			return setter.name();
		
		return null;
	}
	
	public static Class<?> getSetterType( Method m)
	{		
		Setter setter = m.getAnnotation( Setter.class);
		if(setter==null)
			return null;
		
		if(m.getParameterTypes().length > 1)
			return null;
		
		if( !void.class.equals(m.getReturnType()) )
			return null;
		
		return m.getParameterTypes()[0];
	}
	
	public static Class<?> getGetterType( Method m)
	{	
		Getter getter = m.getAnnotation( Getter.class);
		if(getter==null)
			return null;
		
		if(m.getParameterTypes().length > 0)
			return null;
		
		return m.getReturnType();
	}
	
	static class Parameter {
		final String name;
		final Object parent;
		final Method getter;
		final Method setter;
		final Class<?> type;
		
		public Parameter(String name, Object parent, Method getter, Method setter) {
			this.name = name;
			this.parent = parent;
			this.getter = getter;
			this.setter = setter;
			this.type = getSetterType(setter);
			if(!this.type.equals(getGetterType(getter)))
					throw new IllegalArgumentException("type of getter and setter should match! "+ this.type + "   " + getGetterType(getter) );
		}
		
		public Object get()
		{
			try {
				return getter.invoke(parent);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		public void set(Object o)
		{
			try {
				setter.invoke(parent, o);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
	    final List<Method> methods = new ArrayList<Method>();
	    Class<?> klass = type;
	    while (klass != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
	        // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
	        final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));       
	        for (final Method method : allMethods) {
	            if (method.isAnnotationPresent(annotation)) {
	                Annotation annotInstance = method.getAnnotation(annotation);
	                // TODO process annotInstance
	                methods.add(method);
	            }
	        }
	        // move to the upper class in the hierarchy in search for more methods
	        klass = klass.getSuperclass();
	    }
	    return methods;
	}
}
