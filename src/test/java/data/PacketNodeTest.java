/**
 * 
 */
package data;

import static org.junit.Assert.*;

import org.junit.Test;

import data.exceptions.InvalidDataTypeException;
import data.implementation.PacketNode;

/**
 * @author simon
 *
 */
public class PacketNodeTest
{
	int x;
	SampleList output;
	/**
	 * Test method for {@link data.implementation.RMSDataProvider#newData(java.lang.Double)}.
	 * @throws InvalidDataTypeException 
	 */
	@Test
	public void testNewData() throws InvalidDataTypeException
	{
		PacketNode packetNode = new PacketNode(50, 0);
		x =0;
		output = null;
		
		class MyInput extends Input<SampleList>
		{

			MyInput( Node parent)
			{
				super("testListener", SampleList.class, parent);
			}

			@Override
			public void newData(SampleList data)
			{
				x++; output=data;
				assertEquals("Wrong qty in output value", 50, data.size());
			}
		}
		
		Node parent = new Node("parent") {
			{
				inputs.add(new MyInput( this));
				outputs.add(new Output<Integer>("output", Integer.class, this));
			}
		};
		
		packetNode.getOutputs().get(0).connect(parent.getInputs().get(0));
		

		for( Integer i =0; i<255; i++)
		{
			
			((PacketNode.MyInput)packetNode.getInputs().get(0)).newData(new Sample( i.doubleValue(), 0, new Sample.ScalingInformation.SimpleScaling()));
		}
		
		assertEquals("Wrong number of packet generated...", 5,x);
		assertEquals("Wrong qty in output value", 5, output.size());
		
	}
	
}
