/**
 * 
 */
package data.node;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author simon
 *
 */
public interface Disposable {
	
	void dispose();
	
	public static interface DisposableEventListener
	{
		void wasDisposed(Object o);
	}
	
	public static interface DisposableEventProvider extends Disposable
	{
		void addDisposeListener(DisposableEventListener listener);
		void removeDisposeListener(DisposableEventListener listener);
	}
	
	public static abstract class DisposableSupport implements DisposableEventProvider
	{
		protected List<DisposableEventListener> listeners = new CopyOnWriteArrayList<>();
		protected boolean disposed = false;
		
		@Override
		public void addDisposeListener(DisposableEventListener listener)
		{
			listeners.add(listener);
		}
		
		@Override
		public void removeDisposeListener(DisposableEventListener listener)
		{
			listeners.remove(listener);
		}
		
		@Override
		public void dispose()
		{
			for( DisposableEventListener l : listeners)
				l.wasDisposed(this);
			
			disposed = true;
			listeners.clear();
		}
		
		public boolean isDisposed()
		{
			return disposed;
		}
	}
	

}
