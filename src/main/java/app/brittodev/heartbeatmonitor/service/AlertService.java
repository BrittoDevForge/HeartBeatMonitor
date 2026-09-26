package app.brittodev.heartbeatmonitor.service;


import app.brittodev.heartbeatmonitor.entity.Check;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlertService {

    public void alertDown(Check check) {
        log.atInfo().log("Alert : Check is down for " + check.getName());
    }

    public void alertRecovery(Check check) {
        log.atInfo().log("Alert: Check is recovered for " + check.getName());
    }

}
