/**
 * 
 */
package com.webcontext.test.framework.ut;

import static com.jayway.restassured.RestAssured.get;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.jayway.restassured.RestAssured;

/**
 * @author 212391884
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class test_1_RestHander {

	private TestServer ts;

	/**
	 * Start the Server on port 8787 and tyhe mongoDB server on 27117.
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ts = new TestServer();
		ts.start(new String[] { "port=8787", "dbport=27117", "dbembedded=true" });
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		ts.getAppServer().stop();
		ts = null;
	}

	/**
	 * Test method for
	 * {@link com.webcontext.framework.appserver.services.web.response.handler.impl.rest.RestHandler#statistics(com.webcontext.framework.appserver.services.web.server.GenericServer.HttpStatus, com.webcontext.framework.appserver.services.web.http.HttpRequest)}
	 * .
	 */
	@Test
	public void test_1_Statistics() {
		assertTrue("Statistics are not available",get("http://localhost:8787/rest/admin?command=info").statusCode()==200);
	}

	/**
	 * Test method for
	 * {@link com.webcontext.framework.appserver.services.web.response.handler.impl.rest.RestHandler#get(com.webcontext.framework.appserver.services.web.http.HttpRequest, com.webcontext.framework.appserver.services.web.response.RestResponse)}
	 * .
	 */
	@Test
	public void testGet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.webcontext.framework.appserver.services.web.response.handler.impl.rest.RestHandler#post(com.webcontext.framework.appserver.services.web.http.HttpRequest, com.webcontext.framework.appserver.services.web.response.RestResponse)}
	 * .
	 */
	@Test
	public void testPost() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.webcontext.framework.appserver.services.web.response.handler.impl.rest.RestHandler#put(com.webcontext.framework.appserver.services.web.http.HttpRequest, com.webcontext.framework.appserver.services.web.response.RestResponse)}
	 * .
	 */
	@Test
	public void testPut() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.webcontext.framework.appserver.services.web.response.handler.impl.rest.RestHandler#delete(com.webcontext.framework.appserver.services.web.http.HttpRequest, com.webcontext.framework.appserver.services.web.response.RestResponse)}
	 * .
	 */
	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

}
