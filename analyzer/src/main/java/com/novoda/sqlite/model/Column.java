package com.novoda.sqlite.model;

import java.util.Locale;

public final class Column {
    private final String name;
    private final String type;
    private final boolean nullable;
    private final DataAffinity affinity;

    public Column(String name, String type, boolean nullable) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.affinity = DataAffinity.fromType(type);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isNullable() {
        return nullable;
    }

    public DataAffinity getAffinity() {
        return affinity;
    }

    public boolean isBoolean() {
        return affinity == DataAffinity.NUMERIC && type.toLowerCase(Locale.US).contains("bool");
    }


}
