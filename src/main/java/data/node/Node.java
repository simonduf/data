/**
 * 
 */
package data.node;

import java.util.Map;

/**
 * @author Simon Dufour
 *
 */
public interface Node {
	public String getNodeName();
	
	default Map<String, Output<?>> getOutputs()
	{
		return null;
	}
	
	default Map<String, Input<?>> getInputs()
	{
		return null;
	}
}
