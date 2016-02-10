/**
 * 
 */
package data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author simon
 *
 */
public class SampleArrayList extends ArrayList<Sample> implements SampleList
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3131169433298588808L;

	@Override
	public List<Double> getListOfValues()
	{
		List<Double> list = new ArrayList<Double>();
		for(Sample s: this)
		{
			list.add(s.getValue());
		}
		return list;
	}
}
