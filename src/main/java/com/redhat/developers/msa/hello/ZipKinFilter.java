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

import java.util.EnumSet;

import javax.inject.Inject;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.DispatcherType;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.http.DefaultSpanNameProvider;
import com.github.kristofa.brave.servlet.BraveServletFilter;

@WebListener
public class ZipKinFilter implements ServletContextListener {

    @Inject
    private Brave brave;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Dynamic filterRegistration = sce.getServletContext().addFilter("BraveServletFilter",
            new BraveServletFilter(brave.serverRequestInterceptor(), brave.serverResponseInterceptor(), new DefaultSpanNameProvider()));
        // Explicit mapping to avoid trace on readiness probe
        filterRegistration.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/api/hello", "/api/hello-chaining");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
