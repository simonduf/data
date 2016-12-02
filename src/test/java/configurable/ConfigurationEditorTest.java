/**
 * 
 */
package configurable;

import static org.junit.Assert.*;

import java.util.Optional;

import javax.swing.JOptionPane;

import org.junit.Test;

import configurable.ConfigurationEditor.Parameter;

/**
 * @author Simon Dufour
 *
 */
public class ConfigurationEditorTest {

	/**
	 * Test method for {@link configurable.ConfigurationEditor#ConfigurationEditor(java.lang.Object)}.
	 */
	@Test
	public void testConfigurationEditor() {
		
		class DummyClass {
			int i =2;
			@Setter(name = "Integer")
			public void setInt(int i)
			{
				this.i = i;
			}
			
			@Getter(name = "Integer")
			public int getInt(){return i;}
		}
		
		Object o = new DummyClass();
		ConfigurationEditor cfgEdit = new ConfigurationEditor(o);
		
		assertFalse(cfgEdit.parameters.isEmpty());
		assertTrue(cfgEdit.parameters.stream().filter(parameter -> parameter.name.equals("Integer")).count()==1 );
	}
}
