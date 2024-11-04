package org.multiple.datasource.configuration.db.request;

public interface RequestBuilder<T> {
    T build();


    RequestBuilder<T> select(String... fields);

    <R> RequestBuilder<T> from(Class<R> dao);
}
