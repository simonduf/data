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
	
	default Map<String, Output<?>> getDynamicOutputs()
	{
		return null;
	}
	
	default Map<String, Input<?>> getDynamicInputs()
	{
		return null;
	}
}
