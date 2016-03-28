/**
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.developers.msa.hello;

import com.netflix.hystrix.HystrixCommandProperties;
import feign.hystrix.HystrixFeign;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Path("/api")
public class HelloResource {

	/**
	 * The next REST endpoint URL of the service chain to be called.
	 */
	private static final String NEXT_ENDPOINT_URL = "http://namaste:8080/api/namaste-chaining";

	/**
	 * Setting Hystrix timeout for the chain in 1250ms (we have 5 more chained service calls).
	 */
	static {
		HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(1250);
	}

	@GET
	@Path("/hello")
	@Produces("text/plain")
	public String hello() {
		String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
		return String.format("Hello from %s", hostname);
	}

	@GET
	@Path("/hello-chaining")
	@Produces("application/json")
	public List<String> helloChaining() {
		List<String> greetings = new ArrayList<>();
		greetings.add(hello());
		greetings.addAll(createFeign().greetings());
		return greetings;
	}

	/**
	 * This is were the "magic" happens: it creates a Feign, which is a proxy interface for remote
	 * calling a REST endpoint with Hystrix fallback support.
	 *
	 * @return The feign pointing to the service URL and with Hystrix fallback.
	 */
	private ChainedGreeting createFeign() {
		return HystrixFeign.builder().target(ChainedGreeting.class, NEXT_ENDPOINT_URL,
				() -> Collections.singletonList("Namaste response (fallback)"));
	}

}
