package de.zalando.zmon.scheduler.ng.cleanup;

import de.zalando.zmon.scheduler.ng.alerts.AlertChangeListener;
import de.zalando.zmon.scheduler.ng.alerts.AlertDefinition;
import de.zalando.zmon.scheduler.ng.alerts.AlertRepository;
import de.zalando.zmon.scheduler.ng.checks.CheckChangeListener;
import de.zalando.zmon.scheduler.ng.checks.CheckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by jmussler on 02.06.16.
 */
@Component
public class CheckChangeCleaner implements CheckChangeListener {

    private final AlertChangeCleaner alertCleaner;

    private final AlertRepository alertRepository;

    public CheckChangeCleaner(AlertRepository alertRepo, AlertChangeCleaner alertCleaner) {
        this.alertRepository = alertRepo;
        this.alertCleaner = alertCleaner;
    }

    @Override
    public void notifyNewCheck(CheckRepository repo, int checkId) {

    }

    @Override
    public void notifyCheckIntervalChange(CheckRepository repo, int checkId) {

    }

    @Override
    public void notifyDeleteCheck(CheckRepository repo, int checkId) {

    }

    @Override
    public void notifyFilterChange(int checkId) {
        /* Just redirect to clean up all child alerts */
        Collection<AlertDefinition> alertDefs = alertRepository.getByCheckId(checkId);
        for(AlertDefinition ad : alertDefs) {
            alertCleaner.notifyAlertChange(ad);
        }
    }
}