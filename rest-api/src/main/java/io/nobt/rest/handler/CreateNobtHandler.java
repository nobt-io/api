/**
 * 
 */
package io.nobt.rest.handler;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * @author Matthias
 *
 */
public class CreateNobtHandler implements Route {

	/* (non-Javadoc)
	 * @see spark.Route#handle(spark.Request, spark.Response)
	 */
	@Override
	public Object handle(Request req, Response resp) throws Exception {
		return "Hello CreateNobtHandler " + this ;
	}

}
