/**
 * 
 */
package data.annotations;

import java.util.function.Consumer;

/**
 * 
 * @author sdufour
 *
 */
public abstract class Input<T> implements Consumer<T> {
	
	private Consumer<T> destination;
	
	public Input(Consumer<T> destination)
	{
		this.destination = destination;
	}
	
	 public void accept( T object)
	 {
		 destination.accept(object);
	 }
}
