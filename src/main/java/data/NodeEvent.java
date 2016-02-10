package data;

/**
 * @author sdufour
 *
 */
public abstract class NodeEvent {

	final Node source;

	NodeEvent(Node source) {
		this.source = source;
	}

	public Node getSource() {
		return source;
	}

	public static class DisposeEvent extends NodeEvent {

		DisposeEvent(Node source) {
			super(source);
		}
	}

	public static class ConnectEvent extends NodeEvent {

		ConnectEvent(Node source) {
			super(source);
		}
	}

	public static class DisconnectEvent extends NodeEvent {

		DisconnectEvent(Node source) {
			super(source);
		}
	}

}
