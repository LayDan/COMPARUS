package org.multiple.datasource.configuration.db.request;

import java.lang.reflect.Method;
import java.util.List;

public interface Request {

    List<String> getSelectedFields();

    Class<?> getFrom();

}
