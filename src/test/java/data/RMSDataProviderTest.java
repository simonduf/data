/**
 * 
 */
package data;

import static org.junit.Assert.*;

import org.junit.Test;

import data.exceptions.InvalidDataTypeException;
import data.implementation.RmsFromListNode;

/**
 * @author simon
 *
 */
public class RMSDataProviderTest
{
	int x;
	Sample output;
	/**
	 * Test method for {@link data.implementation.RMSDataProvider#newData(java.lang.Double)}.
	 * @throws InvalidDataTypeException 
	 */
	@Test
	public void testNewData() throws InvalidDataTypeException
	{
		RmsFromListNode rms = new RmsFromListNode();
		x =0;
		output = null;
		
		class MyInput extends Input<Sample>
		{

			MyInput( Node parent)
			{
				super("testListener", Sample.class, parent);
			}

			@Override
			public void newData(Sample data)
			{
				x++; output=data;
			}
		}
		
		Node parent = new Node("parent") {
			{
				inputs.add(new MyInput( this));
				outputs.add(new Output<Integer>("output", Integer.class, this));
			}
		};
		
		rms.getOutputs().get(0).connect(parent.getInputs().get(0));
		
		SampleList data = new SampleArrayList();
		for( int i =0; i<255; i++)
		{
			data.add(new Sample(1.0, 1, new Sample.ScalingInformation.SimpleScaling()));
		}
		((RmsFromListNode.MyInput)rms.getInputs().get(0)).newData(data);
		assertTrue("rms trigered too much or not at all...", x==1);
		assertTrue("Wrong rms value", output.getCount() ==1);
		
		
	}
	
}
