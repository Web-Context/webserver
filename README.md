# README

## GenericServer

the GenericServer component is a small and dependency reduced HTTP server, serving JSON document. 
Based only on the HttpServer implementation from Java JDK7, and the Google JSon library (GSON) it brings to 
developer some basic features :

- Rest request handling with simple Implementation,
- Web HTML based pages serving, containing HTML, CSS, and Javascript files, but also images resources like JPG and PNG.
- Other resource would be serve as 

### MIME Type

the WebHandler component, serving pages, supports the following MIME Types:

<table>
<thead><tr><td>MIME type</td><td>File served</td></tr></thead>
<tbody>
<tr><td>application/javascript</td><td>*.js file containing javascript</td></tr>
<tr><td>text/html</td><td>*.html file containing Web page structure</td></tr>
<tr><td>text/css</td><td>*.css file containing Cascades Styles Sheet</td></tr>
<tr><td>application/javascript</td><td>*.js file containing javascript</td></tr>
</tbody>
</table>


## Basic Usage

to start the server you can create a main function (if needed) instantiating the server with some basic parameters:

```java
	public static void main(String[] args) {
	
			GenericServer server;

			int port = 0;

			String stopKey = "";

			try {
				// Retrieve from args from command line like listening port
				port = GenericServer.getIntArg(args, "port", 8888);
				// and the magic keyword to stop server on receiving this one.
				stopKey = GenericServer.getStringArg(args, "StopKey", "STOP");

				// create the Rest Server
				server = new GenericServer(port, stopKey);
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



first implements a RestHandler and add its instance to the GenericServer:

	``` java
	public class GamesRestHandler extends RestHandler {

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
	server.addContext("/rest/foo", new GamesRestHandler(server));
	```

Then, implements the corresponding method into the RestHandler. See bellow for a sample implementation serving


	```java
	public class GamesRestHandler extends RestHandler {
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

# Administration

The server can be called on the localhost:[port]/rest/admin to perform some administratiove operation like:

## Usage Statistics

* ``localhost:[port]/rest/admin?command=info``  return a json structure containing some basic usage statistics and information

	```
	{
	  "info": {
	    "StartDate": "Aug 11, 2014 11:38:25 AM",
	    "LastRequest": "Aug 11, 2014 11:38:31 AM",
	    "RequestCounter": 1,
	    "SuccessfulRequestCounter": 1,
	    "ErrorRequestCounter": 0,
	    "LastURI": "/rest/instruments?title\u003dtest\u0026nb\u003d6",
	    "LastErrorURI": null
	  }
	} 
	```  

where :

* **StartsDate** is the date of last start of the server,
* **LastRequest** is the date where and URL was called on the server (``/admin`` url are excluded from statistics),
* **RequestCounter** is a request ... counter :),
* **SuccessfullRequestCounter** show how many request drives to HTTP status 200 (HttpStatus.OK),
* **ErrorRequestCounter** show number of request driving to Error status (not HTTP 200 code),
* **LastURI** is the last served URI,
* **LastErrorURI** is the last URI called driven to an HTTP error code (not 200).


> **Information**
>
> Calling this url ``localhost:[port]/rest/admin`` with a ``POST`` method drive to the same result.


## Stop server

* ``localhost:[port]/rest/admin?command=stop`` drive to ask stopping the RestHTTP server.
* 
