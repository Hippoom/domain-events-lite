package org.dbaaq.domain;

public class SerializedObject {

    private String type;
    private String data;

    public SerializedObject(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
