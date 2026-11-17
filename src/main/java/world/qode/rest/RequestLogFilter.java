package world.qode.rest;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Writes one JSON line to stdout for every finished HTTP request, health checks included:
 * {"method":"GET","path":"/health","status":200,"durationMs":1.23,"ua":"curl/8.5.0"}
 *
 * KEEP THIS: one log line per request (status + duration) is what the workspace Monitoring tab charts.
 * Never add request/response bodies, cookies, Authorization headers or query strings here.
 */
@WebFilter(urlPatterns = "/*")
public class RequestLogFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        long start = System.nanoTime();
        int status = 500;
        try {
            chain.doFilter(request, response);
            status = response.getStatus();
        } finally {
            double durationMs = Math.round((System.nanoTime() - start) / 10_000.0) / 100.0;
            System.out.println("{\"method\":\"" + json(request.getMethod()) + "\",\"path\":\""
                    + json(request.getRequestURI()) + "\",\"status\":" + status + ",\"durationMs\":" + durationMs
                    + ",\"ua\":\"" + json(request.getHeader("User-Agent")) + "\"}");
        }
    }

    private static String json(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder b = new StringBuilder(s.length());
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> b.append("\\\"");
                case '\\' -> b.append("\\\\");
                default -> {
                    if (c < 0x20) {
                        b.append(String.format("\\u%04x", (int) c));
                    } else {
                        b.append(c);
                    }
                }
            }
        }
        return b.toString();
    }
}
