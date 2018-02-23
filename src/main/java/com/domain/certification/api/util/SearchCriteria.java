package com.domain.certification.api.util;

import com.domain.certification.api.util.enumerator.SearchOperation;

public class SearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;

    public SearchCriteria(String key, SearchOperation operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public SearchOperation getOperation() {
        return operation;
    }

    public Object getValue() {
        return value;
    }
}
