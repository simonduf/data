/**
 * 
 */
package data.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;

/**
 * @author Simon Dufour
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Node{
	String name();
}
