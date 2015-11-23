package org.domaineventslite.jobqueue;

import lombok.Getter;

@Getter
public class FooContext {
    private String bar;

    public FooContext(String bar) {
        this.bar = bar;
    }

    public FooContext() {
    }
}
