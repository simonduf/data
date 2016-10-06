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
		
		/**
		 * @return the specified unit or null if none was set
		 */
		public Unit getUnit();
		
		/**
		 * @return The sample rate or 0 if no sample rate was set
		 */
		public double getSampleRate();
		
		public class SimpleScaling implements ScalingInformation
		{

			private final double valueCoefficient;
			private final double ValueOffset;
			private final double timeCoefficient;
			private final double sampleRate;
			private final Unit unit;
			
			@Deprecated
			public SimpleScaling(int countMin, double valueMin, int countMax, double valueMax, final double timeCoefficient)
			{
				this.valueCoefficient = (valueMax-valueMin)/(countMax-countMin);
				this.ValueOffset = (this.valueCoefficient * countMax);
				this.timeCoefficient = timeCoefficient;
				
				//TODO: fixme
				unit = null;
				sampleRate = 0;
			}
			
			@Deprecated
			public SimpleScaling(final double valueCoefficient, final double valueOffset, final double timeCoefficient)
			{
				this(valueCoefficient, valueOffset, timeCoefficient, null, 0);
			}
			
			public SimpleScaling(final double valueCoefficient, final double valueOffset, final double timeCoefficient, final Unit unit, final double sampleRate)
			{
				this.valueCoefficient = valueCoefficient;
				this.ValueOffset = valueOffset;
				this.timeCoefficient = timeCoefficient;
				this.unit = unit;
				this.sampleRate = sampleRate;
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
				return unit;
			}
			
			@Override
			public double getSampleRate()
			{
				return sampleRate;
			}
			
		}
		

	}
	
	public enum Unit 
	{ 
		VOLT("Volt", "V"),
		AMPS("Amperes", "A");
		
		String shortName;
		String longName;
		
		Unit(String shortName, String longName)
		{
			this.shortName = shortName;
			this.longName = longName;
		}
		
		@Override
		public String toString() {
	        return longName;
	    }
		
		public String getShortName()
		{
			return shortName;
		}
	}
	
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
