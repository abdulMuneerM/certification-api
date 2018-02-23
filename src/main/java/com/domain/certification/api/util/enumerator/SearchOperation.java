package com.domain.certification.api.util.enumerator;

public enum SearchOperation {

    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS, IS_NULL, IS_EMPTY;

    public static final String[] SIMPLE_OPERATION_SET = {":", "!", ">", "<", "~", "@", ";"};

    public static SearchOperation getSimpleOperation(char input) {
        switch (input) {
            case ':':
                return EQUALITY;
            case '!':
                return NEGATION;
            case '>':
                return GREATER_THAN;
            case '<':
                return LESS_THAN;
            case '~':
                return LIKE;
            case '@':
                return IS_NULL;
            case ';':
                return IS_EMPTY;
            default:
                return null;
        }
    }
}
