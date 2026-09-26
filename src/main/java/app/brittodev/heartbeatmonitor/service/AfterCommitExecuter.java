package app.brittodev.heartbeatmonitor.service;


import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class AfterCommitExecuter {
    public void execute(Runnable action) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                       action.run();
                    }
                }
        );
    }
}
