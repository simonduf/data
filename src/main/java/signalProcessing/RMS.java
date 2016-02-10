package signalProcessing;

import java.util.List;

/**
 * Some static utility to calculate rms value.
 * 
 * @author sdufour
 */
public class RMS {
	private RMS(){}
	/**
	 * @param nums
	 *            the numbers use to calculate the RMS value
	 * @return the rms value
	 */
	public static double rms(List<Double> nums) {
		double ms = 0;
		for (Double n : nums) {
			ms += n * n;
		}
		ms /= nums.size();
		return Math.sqrt(ms);
	}
}
