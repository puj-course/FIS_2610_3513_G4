package com.ceiba.fashtoll.worldModel.admin.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class QualityMetricsFilter extends OncePerRequestFilter {

    private final QualityMetricsTracker metricsTracker;

    @Autowired
    public QualityMetricsFilter(QualityMetricsTracker metricsTracker) {
        this.metricsTracker = metricsTracker;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // No medir las peticiones que consultan las mismas métricas
        if (request.getRequestURI().contains("/api/v1/quality")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Deja que la petición siga su curso normal por los controladores
            filterChain.doFilter(request, response);
        } finally {
            // Una vez termina la ejecución, revisamos el código de estado HTTP
            int status = response.getStatus();

            if (status >= 200 && status < 300) {
                metricsTracker.incrementSuccess();
            } else if (status >= 400) {
                metricsTracker.incrementFailure();
            }
        }
    }
}