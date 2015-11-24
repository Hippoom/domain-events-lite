package org.dbaaq.domain;

public interface JobHandler<T> {

    void handle(T jobContext);

}
