/**
 * 
 */
package data.implementation;

import signalProcessing.RMS;
import data.Input;
import data.Node;
import data.Output;
import data.Sample;
import data.SampleList;

/**
 * Calculate the RMS value of the list in input.
 * @author sdufour
 *
 */
public class RmsFromListNode extends Node
{
	
	Input<SampleList>	input	= new MyInput("SampleList", this);
	Output<Sample>		output	= new Output<Sample>("Sample", Sample.class, this);
	
	public RmsFromListNode()
	{
		super("RmsFromListNode");
		
		inputs.add(input);
		outputs.add(output);
	}
	
	public class MyInput extends Input<SampleList>
	{
		
		MyInput(String name, Node parent)
		{
			super(name, SampleList.class, parent);
		}
		
		@Override
		public void newData(SampleList data)
		{
			((RmsFromListNode) getParent()).newData(data);
		}
	}
	
	public void newData(SampleList data)
	{
		Double rms = RMS.rms(data.getListOfValues());
		output.send(new Sample(rms, data.get(0).getSampleID(), data.get(0).getScaling()));
		
	}
}
