package data.annotations;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReflexionConnectionManagerTest {



	@Test
	public void testIsNode() {
		ReflexionConnectionManager cm = new ReflexionConnectionManager();
		
		class NotANode extends Object {};
		
		@Node(name = "The Node!")
		class DefinitlyANode extends Object {};
		
		NotANode notANode = new NotANode();
		DefinitlyANode ANode = new DefinitlyANode();
		
		
		assertFalse(cm.isNode(notANode));
		assertTrue(cm.isNode(ANode));
	}
	

	@Test
	public void testGetOutput() {
		ReflexionConnectionManager cm = new ReflexionConnectionManager();
		
		class ANode extends Object {
			public Output<?> output = new Output<Double>(){};
		};
		
		ANode node = new ANode();
		
		assertTrue(node.output == cm.getOutputByName(node, "output"));
	}

	@Test
	public void testGetOutputType() {
		ReflexionConnectionManager cm = new ReflexionConnectionManager();
		
		class ANode extends Object {
			public Output<?> output = new Output<Double>(){};
		};
		
		ANode node = new ANode();
				
		assertTrue(Double.class == cm.getOutputType(node.output));
	}
	
	
	@Test
	public void testGetInputType() {
		ReflexionConnectionManager cm = new ReflexionConnectionManager();
		
		class ANode extends Object {
			public Input<Double> input = new Input<Double>(this::processData){};
			private void processData(Double d){	}
		};
		
		ANode node = new ANode();
		
		assertEquals(Double.class ,cm.getInputType(node.input));
	}
	
	@Test
	public void testConnect() {
		ReflexionConnectionManager cm = new ReflexionConnectionManager();
		
		@Node(name = "The Node!")
		class ANode extends Object {
			@SuppressWarnings("unused")
			public Input<Double> input = new Input<Double>(this::processData){};
			@SuppressWarnings("unused")
			public Output<?> output = new Output<Double>(){};
			private void processData(Double d){	}
		};
		
		ANode node = new ANode();
		ANode anotherNode = new ANode();
		
		cm.connect(node, "input", anotherNode, "output");
	}
}