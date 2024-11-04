package org.multiple.datasource.configuration.db;

public enum Strategy {
    POSTGRES("SELECT 1");

    private final String testQuery;

    public String getTestQuery() {
        return testQuery;
    }

    Strategy(String testQuery) {
        this.testQuery = testQuery;
    }
}