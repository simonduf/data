/**
 * 
 */
package data.sample;

/**
 * This class is meant to represent a sample taken from an ADC It should
 * include scaling informations for scaling and provide easy methods for
 * manipulating the data. It is immutable.
 * @author simon
 *
 */
public class Sample
{
	public final Value x;
	public final Value y;
	public final double sampleRate;
	
	public Sample( Value x, Value y, double sampleRate)
	{
		this.sampleRate=sampleRate;
		this.x = x;
		this.y = y;
	}
	
	static public class Value
	{
		public final Unit 	unit;
		public final double	value;
		public final Value baseValue;
		
		public Value(Unit unit, double value, Value baseValue)
		{
			this.unit= unit;
			this.value= value;
			this.baseValue = baseValue;
		}
	}
}
