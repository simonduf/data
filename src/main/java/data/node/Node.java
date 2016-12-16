/**
 * 
 */
package data.node;

import java.util.Map;

/**
 * This interface need to be implemented by any class that contain inputs and
 * outputs in order to advertise the features.
 *
 * It also forces the implementation to provide a "pretty name" to display on
 * the screen when required instead of relying on toString()
 *
 */
public interface Node {
	
	/**
	 * @return the name of the class to be displayed to the user
	 */
	public String getNodeName();
	
	/**
	 * Dynamic output are the output that are not a public field in the node.
	 * 
	 * The most frequent scenario were it happen is when the outputs of the node
	 * are generated programmatically
	 * 
	 * Is is expected that this method: 
	 * - return the same map of outputs every time the method is called 
	 * - regular outputs are not returned by this method
	 * 
	 * 
	 * @return a map of the outputs dynamically generated with the name as key,
	 *         or null if there is none.
	 */
	default Map<String, Output<?>> getDynamicOutputs()
	{
		return null;
	}
	
	/**
	 * Dynamic inputs are the inputs that are not a public field in the node.
	 * 
	 * The most frequent scenario were it happen is when the inputs of the node
	 * are generated programmatically
	 * 
	 * Is is expected that this method: 
	 * 		- return the same map of inputs every time the method is called 
	 * 		- regular inputs are not returned by this method
	 * 
	 * @return a map of the inputs dynamically generated with the name as key
	 * 			or null if there is none.
	 */
	default Map<String, Input<?>> getDynamicInputs()
	{
		return null;
	}
}
