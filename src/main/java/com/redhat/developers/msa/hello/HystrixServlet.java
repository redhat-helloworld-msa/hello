package com.redhat.developers.msa.hello;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@WebListener
public class HystrixServlet implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext()
            .addServlet("HystrixMetricsStreamServlet", HystrixMetricsStreamServlet.class)
            .addMapping("/hystrix.stream");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
