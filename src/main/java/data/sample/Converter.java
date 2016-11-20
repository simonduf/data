package data.sample;

import data.sample.Sample.Value;

public interface Converter<Input, Output>
{
	public Output convertValue(Input i);


	public static abstract class ScalingInformation implements Converter<Value, Value> {
		public final Unit outputUnit;
		public final Unit inputUnit;
		public final Converter<Double, Double> converter;

		/**
		 * @param outputUnit
		 * @param inputUnit
		 */
		public ScalingInformation(Unit outputUnit, Unit inputUnit, Converter<Double, Double> converter) {
			this.outputUnit = outputUnit;
			this.inputUnit = inputUnit;
			this.converter = converter;
		}

		public Value convertValue(Value i) {
			return new Value(outputUnit, converter.convertValue(i.value), i);
		}

	}
}