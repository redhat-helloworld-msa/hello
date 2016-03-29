package com.redhat.developers.msa.hello;

import javax.servlet.annotation.WebServlet;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

/**
 * Servlet implementation class HystrixServlet
 */
@WebServlet(
    name = "HystrixMetricsStreamServlet",
    description = "Exposes metrics in a text/event-stream formatted stream that continues as long as a client holds the connection.",
    urlPatterns = { "/hystrix.stream" }
)
public class HystrixServlet extends HystrixMetricsStreamServlet {

    private static final long serialVersionUID = 1L;

}
