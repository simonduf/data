package data.sample;

public enum Unit 
{ 
	VOLT("Volt", "V"),
	AMPS("Amperes", "A"),
	SECONDS("Seconds", "S"),
	COUNT("Count", ""),
	HERTZ("Hertz", "Hz"),
	DEGREE("Degree Celsius", "deg C");
	
	final String shortName;
	final String longName;
	
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