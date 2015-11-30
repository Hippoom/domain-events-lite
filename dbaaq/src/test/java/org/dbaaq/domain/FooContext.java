package org.dbaaq.domain;

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
