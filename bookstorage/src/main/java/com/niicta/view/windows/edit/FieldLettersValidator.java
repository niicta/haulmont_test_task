package com.niicta.view.windows.edit;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

public class FieldLettersValidator implements Validator<String> {
    @Override
    public ValidationResult apply(String s, ValueContext valueContext) {
        if (s.matches("[a-zA-Z\\-\\s]*"))
            return ValidationResult.ok();
        else
            return ValidationResult.error("Must contains only Latin letters");
    }
}
