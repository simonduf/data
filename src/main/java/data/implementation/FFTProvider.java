/**
 * 
 */
package data.implementation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import data.Input;
import data.Node;
import data.Output;
import data.SampleList;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;


/**
 * @author sdufour
 *
 */
public class FFTProvider extends Node {
	Input<SampleList> input = new MyInput("samples", this);
	Output<List<Double>> output = new Output<List<Double>>("ListOfDouble",List.class, this);
	
	
	public FFTProvider()
	{
		super("FFT Provider");
		
		inputs.add(input);
		outputs.add(output);
	}
	
	class MyInput extends Input<SampleList>
	{

		MyInput(String name, Node parent)
		{
			super(name, SampleList.class, parent);
		}

		@Override
		public void newData(SampleList data)
		{
			//TODO pass data directly
			((FFTProvider)getParent()).newData(data.getListOfValues());
			
		}
		
	}

	public void newData(List<Double> data) {
		List<Double> magnitude = new ArrayList<Double>();
		int N = data.size();
		
		DoubleFFT_1D fftDo = new DoubleFFT_1D(data.size());
		double[] fft = new double[data.size() * 2];
		System.arraycopy(ArrayUtils.toPrimitive(data.toArray(new Double[0])), 0, fft, 0, data.size());
		
		fftDo.realForwardFull(fft);
		
		
		for(int i = 0; i<((N/2)-1); i++)
		{
				  double re = fft[2*i];
				  double im = fft[2*i+1];
				  magnitude.add( Math.sqrt(re*re+im*im));
		}
		output.send(magnitude);
	}
}
