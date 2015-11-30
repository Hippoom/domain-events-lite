package org.dbaaq;

import java.util.HashMap;
import java.util.Map;

public class JobSchedulerDispatcher implements JobScheduler {
    private Map<Class<?>, JobScheduler> schedulers = new HashMap();

    @Override
    public void schedule(Object payload) {
        schedulers.get(payload.getClass()).schedule(payload);
    }
}
