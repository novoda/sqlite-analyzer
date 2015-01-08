package com.novoda.sqlite.model;

import com.novoda.sqlite.StringUtil;

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

    public String getCamelizedName() {
        return StringUtil.camelify(name);
    }

    public String getCamelizedSmallName() {
        String camel = StringUtil.camelify(name);
        if (camel.length() <= 1) {
            return "_" + camel;
        }
        return "_" + camel.substring(0, 1).toLowerCase(Locale.US) + camel.substring(1);
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
