package de.zalando.zmon.scheduler.ng.config;

import com.instana.opentracing.InstanaTracer;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import io.opentracing.util.ThreadLocalActiveSpanSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfiguration {

    @Bean
    public Tracer tracer() {
        Tracer tracer = new InstanaTracer(new ThreadLocalActiveSpanSource());
        GlobalTracer.register(tracer);
        return tracer;
    }
}