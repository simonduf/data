package data;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConnectionManagerTest
{
	
	@Test
	public void testEverything()
	{
		ConnectionManager cm = ConnectionManager.getInstance();
		
		// Create some node to work with
		Node[] nodes = new Node[7];
		for (int i = 0; i < 7; i++)
		{
			nodes[i] = getNode();
		}
		
		// Chain them
		for (int i = 0; i < 6; i++)
		{
			cm.connect(nodes[i+1].getInputs().get(0), nodes[i].getOutputs().get(0));
		}
		
		// Verify that they have all be added..
		for (int i = 0; i < 7; i++)
		{
			assertTrue("Node not in ConnectionManager list after connecting them", cm.getNodes().contains(nodes[i]));
		}
		
		cm.keep(nodes[0]);
		cm.keep(nodes[6]);
		
		((MyIntegerInput)nodes[0].getInputs().get(0)).newData( 1 );
		assertEquals( (Integer)1, ((MyIntegerInput)nodes[6].getInputs().get(0)).count );
		
		// Remove the middle one
		cm.dispose(nodes[4]);
		
		// Verify that they are all gone, exept the node to keep
		for (int i = 1; i < 6; i++)
		{
			assertTrue("Node " + i + " in ConnectionManager list after disposing them",
					!cm.getNodes().contains(nodes[i]));
		}
		
		assertTrue("Node not in ConnectionManager while it shoud have been kept", cm.getNodes().contains(nodes[0]));
		assertTrue("Node not in ConnectionManager while it shoud have been kept", cm.getNodes().contains(nodes[6]));
		
		//Dispose the two remaining "keep" node
		cm.dispose(nodes[0]);
		cm.dispose(nodes[6]);
		assertTrue("Node " + 0 + " in ConnectionManager list after disposing them", !cm.getNodes().contains(nodes[0]));
		assertTrue("Node " + 6 + " in ConnectionManager list after disposing them", !cm.getNodes().contains(nodes[6]));
		
		
		//Verify that node are empty at the end, by design and so that other test can start fresh..
		assertTrue(cm.getNodes().isEmpty());
		assertTrue(cm.getkeeplist().isEmpty());
		
	}
	
	class MyIntegerInput extends Input<Integer>
	{
		MyIntegerInput(String name, Node parent)
		{
			super(name, Integer.class, parent);
		}
		public Integer count = 0;
		@SuppressWarnings("unchecked")
		@Override
		public void newData(Integer data)
		{
			((Output<Integer>)getParent().getOutputs().get(0)).send(data);
			count++;
		}
	}
	
	Node getNode()
	{
		Node parent = new Node("parent") {
			{
				inputs.add(new MyIntegerInput("input", this));
				outputs.add(new Output<Integer>("output", Integer.class, this));
			}
		};
		return parent;
	}
	
	Node getNodeDouble()
	{
		class MyInput extends Input<Double>
		{
			MyInput(String name, Node parent)
			{
				super(name, Double.class, parent);
			}
			
			@Override
			public void newData(Double data)
			{
				// method stub
			}
		}
		
		Node parent = new Node("parent") {
			{
				inputs.add(new MyInput("input", this));
				outputs.add(new Output<Integer>("output", Double.class, this));
			}
		};
		return parent;
	}
	
	@Test
	public void testWrongType()
	{
		ConnectionManager cm = ConnectionManager.getInstance();
		Node node1 = getNode();
		Node node2 = getNodeDouble();
		
		Exception e1 = null;
		Exception e2 = null;
		try{
			cm.connect(node1.getInputs().get(0), node2.getOutputs().get(0));
		}
		catch(Exception e) { e1 = e;}
		
		try{
			cm.connect(node2.getInputs().get(0), node1.getOutputs().get(0));
		}
		catch(Exception e) { e2 = e;}
		
		assertNotNull( e1);
		assertNotNull( e2);
		
		cm.dispose(node1);
		cm.dispose(node2);
		
		//Verify that node are empty at the end, by design and so that other test can start fresh..
		assertTrue(cm.getNodes().isEmpty());
		assertTrue(cm.getkeeplist().isEmpty());
	}
}
