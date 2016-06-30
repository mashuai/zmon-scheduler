package de.zalando.zmon.scheduler.ng.checks;

import de.zalando.zmon.scheduler.ng.TokenWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

/**
 * Created by jmussler on 4/2/15.
 */
public class DefaultCheckSource extends CheckSource {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultCheckSource.class);

    private String url;
    private TokenWrapper tokens;
    private boolean isFirstLoad = true;
    private final RestTemplate restTemplate;

    @Autowired
    public DefaultCheckSource(String name, String url, final TokenWrapper tokens, final RestTemplate restTemplate) {
        super(name);
        this.restTemplate = restTemplate;
        LOG.info("configuring check source url={}", url);

        this.url = url;
        this.tokens = tokens;
    }

    private HttpHeaders getWithAuth() {
        HttpHeaders headers = new HttpHeaders();
        if (tokens != null) {
            headers.add("Authorization", "Bearer " + tokens.get());
        }
        return headers;
    }

    @Override
    public Collection<CheckDefinition> getCollection() {
        CheckDefinitions defs = new CheckDefinitions();
        try {
            final String accessToken = tokens.get();
            LOG.info("Querying check definitions with token " + accessToken.substring(0, Math.min(accessToken.length(), 3)) + "..");
            HttpEntity<String> request = new HttpEntity<>(getWithAuth());
            ResponseEntity<CheckDefinitions> response;
            response = restTemplate.exchange(url, HttpMethod.GET, request, CheckDefinitions.class);
            defs = response.getBody();
            LOG.info("Got {} checks from {}", defs.getCheckDefinitions().size(), getName());
            isFirstLoad = false;
        } catch (Throwable t) {
            LOG.error("Failed to get check definitions: {}", t.getMessage());
            if (!isFirstLoad) {
                // rethrow so that currently used checks are still used and not replaced by empty list
                throw t;
            }
        }

        return defs.getCheckDefinitions();
    }
}
