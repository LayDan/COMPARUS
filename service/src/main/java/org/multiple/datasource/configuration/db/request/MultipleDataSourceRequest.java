package org.multiple.datasource.configuration.db.request;


import java.util.List;

public class MultipleDataSourceRequest implements Request {

    private List<String> select;

    private Class<?> from;

    private MultipleDataSourceRequest(List<String> select, Class<?> from) {
        this.select = select;
        this.from = from;
    }

    private MultipleDataSourceRequest() {
    }

    public static Builder builder() {
        return new MultipleDataSourceRequest().new Builder();
    }


    @Override
    public List<String> getSelectedFields() {
        return select;
    }

    @Override
    public Class<?> getFrom() {
        return from;
    }


    public class Builder implements RequestBuilder<MultipleDataSourceRequest> {

        private List<String> select;

        private Class<?> from;

        @Override
        public MultipleDataSourceRequest build() {
            return new MultipleDataSourceRequest(
                    this.select,
                    this.from);
        }

        @Override
        public RequestBuilder<MultipleDataSourceRequest> select(String... fields) {
            this.select = List.of(fields);
            return this;
        }

        @Override
        public <T> RequestBuilder<MultipleDataSourceRequest> from(Class<T> from) {
            this.from = from;
            return this;
        }
    }
}
