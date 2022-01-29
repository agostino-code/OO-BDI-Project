package com.unina.project.graphics;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class LimitedTextField {
    private final IntegerProperty maxLength = new SimpleIntegerProperty(this,
            "maxLength", -1);
    private final StringProperty restrict = new SimpleStringProperty(this, "restrict");

    public LimitedTextField(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<>() {
            private boolean ignore;

            @Override
            public void changed(
                    ObservableValue<? extends String> observableValue,
                    final String s, final String s1) {
                if (ignore || s1 == null)
                    return;
                if (maxLength.get() > -1 && s1.length() > maxLength.get()) {
                    ignore = true;
                    textField.setText(s1.substring(0, maxLength.get()));
                    ignore = false;
                }
                if (restrict.get() != null && !restrict.get().equals("")
                        && !s1.matches(restrict.get() + "*")) {
                    ignore = true;
                    textField.setText(s);
                    ignore = false;
                }
            }
        });
    }

    public IntegerProperty maxLengthProperty() {
        return maxLength;
    }

    public int getMaxLength() {
        return maxLength.get();
    }

    public void setMaxLength(int maxLength) {
        this.maxLength.set(maxLength);
    }

    public StringProperty restrictProperty() {
        return restrict;
    }

    public String getRestrict() {
        return restrict.get();
    }

    public void setRestrict(String restrict) {
        this.restrict.set(restrict);
    }

    public void setStandardField() {
        this.setRestrict("[A-Za-z0-9._-]");
    }

    public void setStandardFieldwSpace() {
        this.setRestrict("[A-Za-z0-9 ._-]");
    }

    public void setCharsOnlyFieldwSpace() {
        this.setRestrict("[A-Za-z ._-]");
    }

    public void setCharsOnlyField() {
        this.setRestrict("[A-Za-z._-]");
    }

    public void setEmailField() {
        this.setRestrict("[A-Za-z0-9@._-]");
    }

    public void setIntegerField() {
        this.setRestrict("[0-9]");
    }

    public void setFloatField() {
        this.setRestrict("[0-9.]");
    }

    public void setDateField() {
        this.setRestrict("[0-9-/]");
        this.setMaxLength(10);
    }

    public void setPhoneNumberField() {
        this.setRestrict("[0-9]");
        this.setMaxLength(10);
    }

    public void setZipcodeField() {
        this.setRestrict("[A-Za-z0-9/]");
        this.setMaxLength(5);
    }
}