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
		
		class ANode extends Object {
			public Output<?> output = new Output<Double>(){};
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
	public void testConnect() {
		ConnectionManager cm = new ConnectionManager();
		
		class ANode implements Node {
			@SuppressWarnings("unused")
			public Input<?> input = new Input<Double>(this::processData){};
			@SuppressWarnings("unused")
			public Output<?> output = new Output<Double>(){};
			private void processData(Double d){	}
			@Override
			public String getNodeName() {
				return "ANode";
			}
		};
		
		ANode node = new ANode();
		ANode anotherNode = new ANode();
		
		cm.connect(node, "input", anotherNode, "output");
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
}
