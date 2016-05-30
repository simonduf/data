/**
 * 
 */
package data;

/**
 * This class is meant to represent a sample taken from an ADC It should
 * include scaling informations for scaling and provide easy methods for
 * manipulating the data. It is immutable.
 * @author simon
 *
 */
public class Sample
{

	/**
	 * @param count is the raw value from the ADC
	 * @param sampleID is the incremental sequence number.
	 * @param scaling is the object necessary for converting to real value
	 */
	public Sample(int count, long sampleID, ScalingInformation scaling)
	{
		this.count = count;
		this.value = scaling.countToValue(count);
		this.time = scaling.sampleIdToTime(sampleID);
		this.sampleID = sampleID;
		this.scaling = scaling;
	}
	
	/**
	 * @param value is the interpreted value for the sample
	 * @param sampleID is the incremental sequence number.
	 * @param scaling is the object necessary for converting to real value
	 */
	public Sample(double value, long sampleID, ScalingInformation scaling)
	{
		this.count = scaling.valueToCount(value);
		this.value = value;
		this.time = scaling.sampleIdToTime(sampleID);
		this.sampleID = sampleID;
		this.scaling = scaling;
	}
	
	private final int					count;
	private final double				value;
	private final double					time;
	private final long					sampleID;
	private final ScalingInformation	scaling;
	
	public interface ScalingInformation
	{
		public double countToValue(int count);
		
		public int valueToCount(double value);
		
		public double sampleIdToTime(long id);
		
		public long timeToSampleID(double time);
		
		public Unit getUnit();
		
		public class SimpleScaling implements ScalingInformation
		{

			private final double valueCoefficient;
			private final double ValueOffset;
			private final double timeCoefficient;
			
			public SimpleScaling(int countMin, double valueMin, int countMax, double valueMax, final double timeCoefficient)
			{
				this.valueCoefficient = valueMax - (valueMax-valueMin)/(countMax-countMin);
				this.ValueOffset = (this.valueCoefficient * countMax);
				this.timeCoefficient = timeCoefficient;
			}
			
			public SimpleScaling(final double valueCoefficient, final double valueOffset, final double timeCoefficient)
			{
				this.valueCoefficient = valueCoefficient;
				this.ValueOffset = valueOffset;
				this.timeCoefficient = timeCoefficient;
			}
			
			public SimpleScaling()
			{
				this( 1, 0, 1);
			}
			
			@Override
			public double countToValue(int count)
			{
				return ValueOffset + valueCoefficient * count;
			}

			@Override
			public int valueToCount(double value)
			{
				return (int) ((value-ValueOffset)/valueCoefficient);
			}

			@Override
			public long timeToSampleID(double time)
			{
				return (long) (time/timeCoefficient);
			}
			
			@Override
			public double sampleIdToTime(long id)
			{
				return timeCoefficient*id;
			}

			@Override
			public Unit getUnit()
			{
				//TODO
				return null;
			}
			
		}
		

	}
	
	public enum Unit { VOLT	}
	
	/**
	 * @return the count
	 */
	public int getCount()
	{
		return count;
	}
	
	/**
	 * @return the value
	 */
	public double getValue()
	{
		return value;
	}
	
	/**
	 * @return the time
	 */
	public double getTime()
	{
		return time;
	}
	
	/**
	 * @return the sampleID
	 */
	public long getSampleID()
	{
		return sampleID;
	}
	
	/**
	 * @return the scaling
	 */
	public ScalingInformation getScaling()
	{
		return scaling;
	}
}
