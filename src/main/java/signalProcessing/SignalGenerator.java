package signalProcessing;

import java.nio.DoubleBuffer;

public class SignalGenerator {
	private SignalGenerator(){}

	/**
	 * Generate a sinewave with the following parameters
	 * 
	 * @param amplitude
	 * @param offset
	 * @param frequency
	 * @param sampleRate
	 * @param samples
	 *            is the number of sample to fill. If this value is set to 0, a
	 *            full sinewave will be generated
	 * @return The Sinewave in a buffer
	 */
	public static DoubleBuffer sinewave(double amplitude, double offset, double frequency, double sampleRate,
			int samples) {
		int sampleQty = samples;
		// If no sample qty was defined, generate a full oscilation
		if (sampleQty == 0) {
			sampleQty = (int) (sampleRate / frequency);
		}

		DoubleBuffer buffer = DoubleBuffer.allocate(sampleQty);
		for (int i = 0; i < sampleQty; i++) {
			buffer.put(offset + amplitude * Math.sin(2 * Math.PI * frequency * i / sampleRate));
		}
		return buffer;
	}

	/**
	 * Calculate the maximum sample rate for generating a specific frequency
	 * considering a maximum buffer size. This method is interesting to have the
	 * best resolution for a signal. Values are rounded/trimmed down. (As long
	 * as they are positive, witch they should...)
	 * 
	 * @param maxBufferSize
	 *            is the maximum number of sample in the buffer.
	 * @param MaxSampleRate
	 *            is the maximum dac frequency at witch the hardware can go
	 * @param lowerFrequency
	 *            is the lower frequency that we want to reproduce
	 * @return the maximum theorical frequency, not considering the hardware
	 *         limitation for the clock itself (see
	 *         calcuclateMaxSampleSampleRate for this)
	 */
	public static int calcuclateMaxSampleSampleRate(int maxBufferSize, int MaxSampleRate, double lowerFrequency) {
		int sampleRate = (int) (maxBufferSize * lowerFrequency);
		return (sampleRate > MaxSampleRate) ? MaxSampleRate : sampleRate;
	}

	/**
	 * This function calculate the closest clock that can be obtain, considering
	 * only a clock divider by whole numbers.
	 * 
	 * @param mainClock
	 * @param desiredClock
	 * @return the closest clock
	 */
	public static int calcuclateCloserPossibleClock(int mainClock, int desiredClock) {
		for (int i = 1; i < 255; i++) {
			if (mainClock / i <= desiredClock) {
				return mainClock / i;
			}
		}
		return desiredClock;
	}

}
