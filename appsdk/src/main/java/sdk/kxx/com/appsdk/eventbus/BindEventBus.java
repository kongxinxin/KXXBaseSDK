package sdk.kxx.com.appsdk.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version : 1.0
 * @Description :  BindEventBus
 * @Create Date  : 19/1/15  15:37
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindEventBus {

}
