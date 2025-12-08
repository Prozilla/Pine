package dev.prozilla.pine.common.property.observable;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.logging.Logger;

public interface SimpleObservableProperty<T> extends ObservableProperty<T>, Destructible {
	
	/**
	 * Sets the logger of this property, which is used to log errors thrown by observers.
	 */
	void setLogger(Logger logger);

}
