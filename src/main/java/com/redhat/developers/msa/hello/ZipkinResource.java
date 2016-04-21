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

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.EmptySpanCollectorMetricsHandler;
import com.github.kristofa.brave.http.HttpSpanCollector;

/**
 * This class uses CDI to alias Zipkin resources to CDI beans
 *
 */
public class ZipkinResource {

    @Produces
    @Singleton
    public Brave getBrave() {
        Brave brave = new Brave.Builder("hello")
            .spanCollector(HttpSpanCollector.create("http://zipkin-query:9411", new EmptySpanCollectorMetricsHandler()))
            .build();
        return brave;
    }

}
