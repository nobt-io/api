package io.nobt.rest;

import static spark.Spark.get;

import io.nobt.rest.handler.CreateNobtHandler;

public class HelloWorld {

	public static void main(String[] args) {
		get("/hello", new CreateNobtHandler());
		get("/hello1", (req, res) -> "Hello World");
	}
	
}
