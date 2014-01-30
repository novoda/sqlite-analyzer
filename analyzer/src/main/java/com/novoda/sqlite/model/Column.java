package com.novoda.sqlite.model;

import com.novoda.sqlite.StringUtil;

public final class Column {
    private final String name;
    private final String type;
    private boolean nullable;
    private DataAffinity affinity;

    public Column(String name, String type, boolean nullable) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.affinity = computeAffinity(type);
    }

    public String getName() {
        return name;
    }

    public String getCamelizedName() {
        return StringUtil.camelify(name);
    }

    public String getCamelizedSmallName() {
        String camel = StringUtil.camelify(name);
        if (camel.length() <= 1)
            return camel;
        return camel.substring(0,1).toLowerCase()+camel.substring(1);
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
        return  affinity == DataAffinity.NUMERIC && type.toLowerCase().contains("bool");
    }

    /*
 * See http://www.sqlite.org/datatype3.html
 * section 2.1 Determination of column affinity
 */
    private DataAffinity computeAffinity(String type) {
        String deftype = type.toLowerCase();
        if (deftype.contains("int"))
            return DataAffinity.INTEGER;
        if (containsOneOf(deftype, "char", "clob", "text"))
            return DataAffinity.TEXT;
        if (containsOneOf(deftype, "real", "floa", "doub"))
            return DataAffinity.REAL;
        if (containsOneOf(deftype, "blob") || deftype.equals(""))
            return DataAffinity.NONE;
        return DataAffinity.NUMERIC;
    }

    private boolean containsOneOf(String toCheck, String... values) {
        for (String value : values) {
            if (toCheck.contains(value))
                return true;
        }
        return false;
    }

}
