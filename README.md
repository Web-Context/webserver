# README

## RestServer

the rest Server component is a small and dependency reduced HTTP server, serving JSON document. 
Based only on the HttpServer implementation from Java SE 7, and the GSon library it brings to 
developer some basic features like Rest request handling with simple Implementation.


## Basic Usage

to start the server you can create a main function (if needed) instantiating the server with some basic parameters:

```java
	public static void main(String[] args) {
	
			RestServer server;

			int port = 0;

			String stopKey = "";

			try {
				// Retrieve from args from command line like listening port
				port = RestServer.getIntArg(args, "port", 8888);
				// and the magic keyword to stop server on receiving this one.
				stopKey = RestServer.getStringArg(args, "StopKey", "STOP");

				// create the Rest Server
				server = new RestServer(port, stopKey);
				// Add some specific Rest handler to process request
				server.addContext("/rest/foo", new FooRestHandler(server));
				// then start server.
				server.start();

			} catch (IOException | InterruptedException e) {
				LOGGER.error("Unable to start the internal Rest HTTP Server component on port "
						+ port + ". Reason : " + e.getLocalizedMessage());
			}
			LOGGER.info("End of processing request in Server on port "+port);
			System.exit(0);
		}
```

## Stop the server

A default RestHandler is listening the ``localhost:8888/rest/admin`` URL. if the parameter ``stop=stop`` is send, the server will automatically stop (see AdminRestHandler class for more information).



first implements a RestHandler and add its instance to the RestServer:

	``` java
	public class MeasuresRestHandler extends RestHandler {

		@Override
		public HttpStatus get(HttpRequest request, RestResponse response){
			return HttpStatus.OK;
		}

		@Override
		public HttpStatus post(HttpRequest request, RestResponse response){
			return HttpStatus.OK;
		}
	
		@Override
		public HttpStatus put(HttpRequest request, RestResponse response){
			return HttpStatus.OK;
		}
	
		@Override
		public HttpStatus delete(HttpRequest request, RestResponse response){
			return HttpStatus.OK;
		}
	
	} 
	```




# Implementation

## HttpRequest

 ``Object getParamater(String name, T defaultValue)`` will extract the ``name`` parameter from the Http request, or if this one does not exist, the ``defaultValue``.
 
## RestResponse 

``RestResponse`` class encapsulates the mechanism to generate JSon from data push to this object. ``HttpResponse`` embeds a ``Map<String,Object>`` containing all object to be return as JSON accordingly to the ``HttpRequest``.

## RestHandler

The ``get()``, ``post()``, ``put()``, ``delete()``, ``options()``, and ``head()`` methods are reflecting some of the standard HTTP Method.

``HttpRequest`` class is a design of the HTTP Request. It contains basically all object from the HttpExchange class from the  ``com.sun.net.httpserver```package, but adding Request parameters and headers easy access helpers.

## Processing GET, POST, PUT, DELETE, etc...

To perform processing of one of the HTTP method on a specific URL, you must implements a RestHandler linked to the "URL" and declare this into the server:

	```java
	server.addContext("/rest/foo", new FooRestHandler(server));
	```

Then, implements the corresponding method into the RestHandler. See bellow for a sample implementation serveing


	```java
	public class FooRestHandler extends RestHandler {
		private static final Logger LOGGER = Logger
			.getLogger(FooRestHandler.class);
		@Override
		public HttpStatus get(
			HttpRequest request, 
			RestResponse response) {
		
			String title = (String) request
				.getParameter(
					"title", "no-title").toArray()[0];
			if( title != "" ){
				LOGGER.debug(
					String.format("Parameters title=%s",
										title));
		
				response.add("title", title);
				return HttpStatus.OK;
			} else {
				response.add("error", "Unable to retrieve data");
				return HttpStatus.NOT_FOUNT;
			}
		} 
 	}
	```
	
This small piece of code will serve the ```localhost:8888/rest/foo```  with a json document : 

	```java
	{ "title": "[title from URL]" }
	```   	


To continued ...   	