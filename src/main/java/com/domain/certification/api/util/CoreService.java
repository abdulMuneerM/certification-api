package com.domain.certification.api.util;

import com.domain.certification.api.util.enumerator.SearchOperation;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CoreService {

    private final String SEARCH_PATTERN;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    public CoreService() {
        StringJoiner joinNames = new StringJoiner("|");
        for (String operation : SearchOperation.SIMPLE_OPERATION_SET) {
            joinNames.add(operation);
        }

//        String operationExpression = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
        String operationExpression = joinNames.toString();
        SEARCH_PATTERN = "(\\w+?)(" + operationExpression + ")(\\*?)([\\w -]+?)(\\*?),";
    }

    public Matcher searchPatternMatcher(String search) {
        Pattern pattern = Pattern.compile(SEARCH_PATTERN);
        return pattern.matcher(search + ",");
    }


    public Long getCurrentEpochSeconds() {
        return Instant.now().getEpochSecond();
    }

    public boolean validatePhoneNumber(String phoneNumber, int length) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return false;
        }

        phoneNumber = phoneNumber.replaceAll("[^\\d]", "");
        phoneNumber = StringUtils.trimWhitespace(phoneNumber);

        if (phoneNumber.length() == length) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }
}
