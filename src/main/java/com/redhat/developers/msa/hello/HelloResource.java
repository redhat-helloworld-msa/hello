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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ServerSpan;
import com.github.kristofa.brave.http.DefaultSpanNameProvider;
import com.github.kristofa.brave.http.StringServiceNameProvider;
import com.github.kristofa.brave.httpclient.BraveHttpRequestInterceptor;
import com.github.kristofa.brave.httpclient.BraveHttpResponseInterceptor;

import feign.Logger;
import feign.Logger.Level;
import feign.httpclient.ApacheHttpClient;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import io.swagger.annotations.ApiOperation;

@Path("/")
public class HelloResource {

    @Inject
    private Brave brave;

    @GET
    @Path("/hello")
    @Produces("text/plain")
    @ApiOperation("Returns the greeting in English")
    public String hello() {
        String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
        return String.format("Hello from %s", hostname);
    }

    @GET
    @Path("/hello-chaining")
    @Produces("application/json")
    @ApiOperation("Returns the greeting plus the next service in the chain")
    public List<String> helloChaining() {
        List<String> greetings = new ArrayList<>();
        greetings.add(hello());
        greetings.addAll(getNextService().namaste());
        return greetings;
    }

    @GET
    @Path("/health")
    @Produces("text/plain")
    @ApiOperation("Used to verify the health of the service")
    public String health() {
        return "I'm ok";
    }

    /**
     * This is were the "magic" happens: it creates a Feign, which is a proxy interface for remote calling a REST endpoint with
     * Hystrix fallback support.
     *
     * @return The feign pointing to the service URL and with Hystrix fallback.
     */
    private NamasteService getNextService() {
        final String serviceName = "namaste";
        // This stores the Original/Parent ServerSpan from ZiPkin.
        final ServerSpan serverSpan = brave.serverSpanThreadBinder().getCurrentServerSpan();
        final CloseableHttpClient httpclient =
            HttpClients.custom()
                .addInterceptorFirst(new BraveHttpRequestInterceptor(brave.clientRequestInterceptor(), new StringServiceNameProvider(serviceName), new DefaultSpanNameProvider()))
                .addInterceptorFirst(new BraveHttpResponseInterceptor(brave.clientResponseInterceptor()))
                .build();
        String url = String.format("http://%s:8080/", serviceName);
        return HystrixFeign.builder()
            // Use apache HttpClient which contains the ZipKin Interceptors
            .client(new ApacheHttpClient(httpclient))
            // Bind Zipkin Server Span to Feign Thread
            .requestInterceptor((t) -> brave.serverSpanThreadBinder().setCurrentSpan(serverSpan))
            .logger(new Logger.ErrorLogger()).logLevel(Level.BASIC)
            .decoder(new JacksonDecoder())
            .target(NamasteService.class, url,
                () -> Collections.singletonList("Namaste response (fallback)"));
    }

}
