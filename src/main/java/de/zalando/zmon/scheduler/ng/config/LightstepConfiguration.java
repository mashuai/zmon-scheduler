package de.zalando.zmon.scheduler.ng.config;

import io.opentracing.Tracer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;

@Configuration
@ConfigurationProperties(prefix = "tracing.lightstep")
public class LightstepConfiguration {
    private String accessToken;
    private String collectorHost = "localhost";
    private int collectorPort = 443;
    private String collectorProtocol = "https";
    private String componentName = "zmon-scheduler-";

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCollectorHost() {
        return collectorHost;
    }

    public void setCollectorHost(final String collectorHost) {
        this.collectorHost = collectorHost;
    }

    public int getCollectorPort() {
        return collectorPort;
    }

    public void setCollectorPort(final int collectorPort) {
        this.collectorPort = collectorPort;
    }

    public String getCollectorProtocol() {
        return collectorProtocol;
    }

    public void setCollectorProtocol(final String collectorProtocol) {
        this.collectorProtocol = collectorProtocol;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(final String componentName) {
        this.componentName = componentName;
    }

    @Bean
    public Tracer lightstepTracer() throws MalformedURLException {
        return new com.lightstep.tracer.jre.JRETracer(
                new com.lightstep.tracer.shared.Options.OptionsBuilder()
                        .withAccessToken(accessToken)
                        .withCollectorHost(collectorHost)
                        .withCollectorPort(collectorPort)
                        .withCollectorProtocol(collectorProtocol)
                        .withComponentName(componentName)
                        .build());
    }
}