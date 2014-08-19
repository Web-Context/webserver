# WebServer

![Travis-CI.org build status](https://travis-ci.org/mcgivrer/webserver.svg?branch=master)

This small piece of code is my first implementation of a minimalistic web server coded over the JDK(>=6).
This server and its sample usage demonstration based on a Game library web site proposes some basic features for a modern web server.

- Web server (java oriented but not JavaEE !)
- Embedded MongoDB server (if parametered).

The ``Server`` class propose an implementation of both services with some arguments on java command:

	```bash

	$> java -jar gamerestserver-0.0.1-SNAPSHOT.jar \
		com.webcontext.apps.grs.application.services.Server \
			port=8888 dbembedded=true dbport=27017 
	```


where arguments are :

| argument      | value     | description                                    |
|---------------|-----------|------------------------------------------------|
| port          | 8888      | this is the Web server port, default is 8888   |
| dbembedded    | true      | Ask to embedded MongoDb server to install, start and intialize.   |
| dbport        | 27017     | dbport set the MongoDB execution port. default is 27017 |



## GenericServer


the GenericServer component is a small and dependency reduced HTTP server, serving JSON document. 
Based only on the HttpServer implementation from Java JDK7, and the Google JSon library (GSON) it brings to 
developer some basic features :

- Rest request handling with simple Implementation,
- Web HTML based pages serving, containing HTML, CSS, and Javascript files, but also images resources like JPG and PNG.
- Other resource would be serve as 

### MIME Type

The WebHandler component, serving pages, supports the following MIME Types:

| MIME Type                | File served                                 |
|--------------------------|---------------------------------------------|
| application/javascript   | *.js file containing javascript             |
| text/html                | *.html file containing Web page structure   |
| text/css                 | *.css file containing Cascades Styles Sheet |
| image/jpeg               | *.jpg,*.jpeg image resource                 |
| image/png                | *.png image resource                        |
| application/octet-stream | *.* others binary resource                  |

## Basic Usage

to start the server you can create a main function (if needed) instantiating the server with some basic parameters:

```java
	public static void main(String[] args) {
		try {

			/**
			 * Initialize and start the MongoDBserver.
			 */
			dbServer = new MongoDBServer(args);
			dbServer.start();
			dbServer.waitUntilStarted();

			// initialize server.
			appServer = new GenericServer(args);

			// Add a new repository.
			DataManager.getInstance()
					.register(Game.class, GameRepository.class);

			// add a new Handler to the Rest Server.
			appServer.addRestContext("/rest/games", new GamesRestHandler(appServer));

			// and start server.
			appServer.start();

		} catch (IOException | InterruptedException | InstantiationException
				| IllegalAccessException e) {
			LOGGER.error("Unable to start the internal Rest HTTP Server component. Reason : "
					+ e.getLocalizedMessage());
		}finally{
			if(appServer != null){
				appServer.stop();
			}
			if(dbServer != null){
				dbServer.stop();
			}
		}
		LOGGER.info("End of processing request in Server");

		// Exit from server.
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

## Rest Service

### HttpRequest

 ``Object getParamater(String name, T defaultValue)`` will extract the ``name`` parameter from the Http request, or if this one does not exist, the ``defaultValue``.
 
### RestResponse 

``RestResponse`` class encapsulates the mechanism to generate JSon from data push to this object. ``HttpResponse`` embeds a ``Map<String,Object>`` containing all object to be return as JSON accordingly to the ``HttpRequest``.

### RestHandler

The ``get()``, ``post()``, ``put()``, ``delete()``, ``options()``, and ``head()`` methods are reflecting some of the standard HTTP Method.

``HttpRequest`` class is a design of the HTTP Request. It contains basically all object from the HttpExchange class from the  ``com.sun.net.httpserver```package, but adding Request parameters and headers easy access helpers.

### Processing GET, POST, PUT, DELETE, etc...

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

## Web Page Service

The first service assured by this small web server implementation, This handler (the ``WebHandler``) is ready to serve real web pages as HTML, CSS and javascript files, and images like JPG and PNG ones. But also will serve any other file ``application/octet-stream``. 

A small properties file exists to set mapping between file extension and MIME types. See previous exposed MIME types table. You will be able to map anay file extension to MIME types.

The mechanism is based on the same implementation as RestHandler.


## Administration

The server can be called on the localhost:[port]/rest/admin to perform some administratiove operation like:

### Usage Statistics

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


### Stop server

* ``localhost:[port]/rest/admin?command=stop`` drive to ask stopping the RestHTTP server.

