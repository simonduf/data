/**
 * 
 */
package data.implementation;

import data.Input;
import data.Node;
import data.Output;
import data.Sample;
import data.SampleArrayList;
import data.SampleList;

/**
 * Accumulate some samples and then send them as List
 * @author sdufour
 *
 */
public class PacketNode extends Node {
	
	
	private int qtyToPacket;
	private int timeToPacket;
	private SampleList	listOfdata = new SampleArrayList();
	Input<Sample> input = new MyInput("samples", this);
	Output<SampleList> output = new Output<SampleList>("SampleList",SampleList.class, this);
	
	
	/**
	 * @param qty the quantity of sample to accumulate before generating the packet
	 * @param time the minimum time to accumulate before sending the packet
	 */
	public PacketNode(int qty, int time)
	{
		super("PacketDataProvider");
		qtyToPacket = qty;
		timeToPacket = time;
		
		inputs.add(input);
		outputs.add(output);
	}
	
	public class MyInput extends Input<Sample>
	{

		MyInput(String name, Node parent)
		{
			super(name, Sample.class, parent);
		}

		@Override
		public void newData(Sample data)
		{
			((PacketNode)getParent()).newData(data);
			
		}
		
	}

	public void newData(Sample data) {
		
		listOfdata.add(data);
		
		int time = 10;
		
		if( (listOfdata.size()>=qtyToPacket) && (time>timeToPacket) )
		{
			output.send(listOfdata);
			listOfdata.clear();
		}
	}
}
