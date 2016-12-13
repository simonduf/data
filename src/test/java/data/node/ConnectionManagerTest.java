package data.node;

import static org.junit.Assert.*;

import org.junit.Test;


public class ConnectionManagerTest {



//	@Test
//	public void testIsNode() {
//		ReflexionConnectionManager cm = new ReflexionConnectionManager();
//		
//		class NotANode extends Object {};
//		
//		@Node(name = "The Node!")
//		class DefinitlyANode extends Object {};
//		
//		NotANode notANode = new NotANode();
//		DefinitlyANode ANode = new DefinitlyANode();
//		
//		
//		assertFalse(cm.isNode(notANode));
//		assertTrue(cm.isNode(ANode));
//	}
	

	@Test
	public void testGetOutput() {
		ConnectionManager cm = new ConnectionManager();
		
		class ANode implements Node {
			public Output<?> output = new Output<Double>(){};

			@Override
			public String getNodeName() {return "ANode";}
		};
		
		ANode node = new ANode();
		
		assertTrue(node.output == cm.getOutputByName(node, "output"));
	}

	@Test
	public void testGetOutputType() {
		ConnectionManager cm = new ConnectionManager();
		
		class ANode extends Object {
			public Output<?> output = new Output<Double>(){};
		};
		
		ANode node = new ANode();
				
		assertTrue(Double.class == cm.getOutputType(node.output));
	}
	
	
	@Test
	public void testGetInputType() {
		ConnectionManager cm = new ConnectionManager();
		
		class ANode extends Object {
			public Input<Double> input = new Input<Double>(this::processData){};
			private void processData(Double d){	}
		};
		
		ANode node = new ANode();
		
		assertEquals(Double.class ,cm.getInputType(node.input));
	}
		@Test
	public void testConnectUsingName() {
		ConnectionManager cm = new ConnectionManager();
		
		class ANode implements Node {
			public Input<?> input = new Input<Double>(this::processData){};
			public Output<?> output = new Output<Double>(){};
			private void processData(Double d){	}
			@Override
			public String getNodeName() {
				return "ANode";
			}
		};
		
		ANode node = new ANode();
		ANode anotherNode = new ANode();
		
		cm.add(node);
		cm.add(anotherNode);
		
		cm.connect(node, "input", anotherNode, "output");
		assertTrue(anotherNode.output.getConnectedInputs().contains(node.input));
		cm.disconnect(node.input, anotherNode.output);
		assertFalse(anotherNode.output.getConnectedInputs().contains(node.input));
	}
		
		
	@Test
	public void testConnectTypeSafe() {
		ConnectionManager cm = new ConnectionManager();
		
		class ANode implements Node {
			public Input<Double> input = new Input<Double>(this::processData){};
			public Output<Double> output = new Output<Double>(){};
			private void processData(Double d){	}
			@Override
			public String getNodeName() {
				return "ANode";
			}
		};
		
		ANode node = new ANode();
		ANode anotherNode = new ANode();
		
		cm.add(node);
		cm.add(anotherNode);
		
		cm.connect(node.input, anotherNode.output);
		assertTrue(anotherNode.output.getConnectedInputs().contains(node.input));
		cm.disconnect(node.input, anotherNode.output);
		assertFalse(anotherNode.output.getConnectedInputs().contains(node.input));
	}
	
	@Test(expected= RuntimeException.class)
	public void testConnectMissingInput() {
		ConnectionManager cm = new ConnectionManager();
		

		class ANode implements Node {
			@SuppressWarnings("unused")
			public Input<?> inputNotWanted = new Input<Double>(this::processData){};
			@SuppressWarnings("unused")
			public Output<?> output = new Output<Double>(){};
			private void processData(Double d){	}
			public String getNodeName() {
				return "ANode";
			}
		};
		
		ANode node = new ANode();
		ANode anotherNode = new ANode();
		
		cm.connect(node, "input", anotherNode, "output");
	}
	
	@Test(expected= RuntimeException.class)
	public void testConnectOfDifferentTypes() {
		ConnectionManager cm = new ConnectionManager();
		
		class IntegerNode implements Node {
			@SuppressWarnings("unused")
			public Input<?> input = new Input<Integer>(this::processData){};
			@SuppressWarnings("unused")
			public Output<?> output = new Output<Integer>(){};
			private void processData(Integer d){	}
			public String getNodeName() {
				return "The Integer Node!";
			}
		};
		

		class DoubleNode implements Node {
			@SuppressWarnings("unused")
			public Input<?> input = new Input<Double>(this::processData){};
			@SuppressWarnings("unused")
			public Output<?> output = new Output<Double>(){};
			private void processData(Double d){	}
			public String getNodeName() {
				return "The Double Node!";
			}
		};
		
		IntegerNode node = new IntegerNode();
		DoubleNode anotherNode = new DoubleNode();
		
		cm.connect(node, "input", anotherNode, "output");
	}
	
	@Test(expected= RuntimeException.class)
	public void testConnectOfDifferentTypes2() {
		ConnectionManager cm = new ConnectionManager();
		

		class IntegerNode implements Node {
			@SuppressWarnings("unused")
			public Input<?> input = new Input<Integer>(this::processData){};
			@SuppressWarnings("unused")
			public Output<?> output = new Output<Integer>(){};
			private void processData(Integer d){	}
			public String getNodeName() {
				return "The Integer Node!";
			}
		};
		
		class DoubleNode implements Node {
			@SuppressWarnings("unused")
			public Input<?> input = new Input<Double>(this::processData){};
			@SuppressWarnings("unused")
			public Output<?> output = new Output<Double>(){};
			private void processData(Double d){	}
			public String getNodeName() {
				return "The Double Node!";
			}
		};
		
		IntegerNode node = new IntegerNode();
		DoubleNode anotherNode = new DoubleNode();
		
		cm.connect(anotherNode, "input", node, "output");
	}
	
	
	
	@Test
	public void testIsConnectable() {
		ConnectionManager cm = new ConnectionManager();
		

		class IntegerNode implements Node {
			public Input<?> input = new Input<Integer>(this::processData){};
			public Output<?> output = new Output<Integer>(){};
			private void processData(Integer d){	}
			public String getNodeName() {
				return "The Integer Node!";
			}
		};
		
		class DoubleNode implements Node {
			public Input<?> input = new Input<Double>(this::processData){};
			public Output<?> output = new Output<Double>(){};
			private void processData(Double d){	}
			public String getNodeName() {
				return "The Double Node!";
			}
		};
		
		IntegerNode intnode = new IntegerNode();
		DoubleNode  doubleNode= new DoubleNode();
		IntegerNode anotherIntNode = new IntegerNode();
		DoubleNode anotherDoubleNode = new DoubleNode();
		
		cm.add(intnode);
		cm.add(doubleNode);
		cm.add(anotherIntNode);
		cm.add(anotherDoubleNode);
		
		assertSame(intnode, cm.getParent(intnode.input));
		assertSame(intnode, cm.getParent(intnode.output));
		
		assertTrue(cm.isConnectable(intnode.input, anotherIntNode.output));
		assertTrue(cm.isConnectable(anotherIntNode.input, intnode.output));
		
		assertTrue(cm.isConnectable(doubleNode.input, anotherDoubleNode.output));
		assertTrue(cm.isConnectable(anotherDoubleNode.input, doubleNode.output));
		
		assertFalse(cm.isConnectable(intnode.input, doubleNode.output));
		assertFalse(cm.isConnectable(doubleNode.input, intnode.output));
		
		assertFalse(cm.isConnectable(doubleNode.input, doubleNode.output));
		assertFalse(cm.isConnectable(intnode.input, intnode.output));
		
	}
	
}
