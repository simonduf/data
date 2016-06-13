/**
 * 
 */
package data;

/**
 * This class is provided to distinguish the objects 
 * @author simon
 *
 */
public abstract class NamedElement
{
	private final String name;
	protected NamedElement(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return name;
	}
}
