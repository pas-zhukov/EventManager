package ru.pas_zhukov.eventmanager.kafka;

public class FieldChange<T> {
    private T oldValue;
    private T newValue;

    public FieldChange() {
    }

    public FieldChange(T oldValue, T newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public T getNewValue() {
        return newValue;
    }

    public void setNewValue(T newValue) {
        this.newValue = newValue;
    }

    public T getOldValue() {
        return oldValue;
    }

    public void setOldValue(T oldValue) {
        this.oldValue = oldValue;
    }
}
