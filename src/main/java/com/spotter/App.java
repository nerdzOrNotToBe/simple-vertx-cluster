package com.spotter;

import io.vertx.core.AbstractVerticle;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dimitri on 03/11/2016.
 */
public class App extends AbstractVerticle {

	public void start() {
		vertx.createHttpServer().requestHandler(request -> {
			String groupName = System.getenv("cluster-name");

			try {
				request.response().end(
						"<!DOCTYPE html>\n" +
						"<html lang=\"en\">\n" +
						"<head>\n" +
						"\t<meta charset=\"UTF-8\">\n" +
						"\t<title>Vertx Cluster</title>\n" +
						"</head>\n" +
						"<body>\n" +
						" <div style=\"align-content: center\">\n" +
						"Hello world" +
						"\t <ul>\n" +
						"\t\t <li>container name : "+InetAddress.getLocalHost().getHostName()+"</li>\n" +
						"\t\t <li>ip : "+ InetAddress.getLocalHost().getHostAddress()+"</li>\n" +
						"\t\t <li>cluster name : "+groupName+"</li>\n" +
						"\t </ul>\n" +
						" </div>\n" +
						"</body>\n" +
						"</html>");
			} catch (UnknownHostException e) {
				System.out.println(e);
			}
		}).listen();
	}


	

	public void stop() {
	}
}
