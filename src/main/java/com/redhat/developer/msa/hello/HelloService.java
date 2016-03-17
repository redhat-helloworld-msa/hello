/**
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
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
package com.redhat.developer.msa.hello;

import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

@Path("/")
public class HelloService {

    @GET
    @Path("/hello")
    public String sayHello() {
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostname = "[local host name could not be resolved into an address]";
        }
        return "Hello from " + hostname;
    }

    @GET
    @Path("/hello-chaining")
    public String sayHelloChaining() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        jab.add(sayHello());
        try {
            String bonJourResponse = getOlaResponse();
            JsonArray responseArray = Json.createReader(new StringReader(bonJourResponse)).readArray();
            responseArray.forEach(service -> jab.add(service));
        } catch (Exception e) {
            jab.add("Generic Ola response");
        }
        return jab.build().toString();
    }

    private String getOlaResponse() throws IOException {
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(2000)
            .setConnectionRequestTimeout(2000)
            .build();
        HttpGet httpGet = new HttpGet("http://ola:8080/ola-chaining");
        httpGet.setConfig(requestConfig);
        HttpClient httpClient = HttpClientBuilder.create().build();
        return EntityUtils.toString(httpClient.execute(httpGet).getEntity());
    }

}
