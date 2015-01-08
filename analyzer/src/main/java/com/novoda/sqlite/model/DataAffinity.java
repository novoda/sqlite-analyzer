package com.novoda.sqlite.model;

import java.util.Locale;

public enum DataAffinity {
    TEXT, NUMERIC, INTEGER, REAL, NONE;

    public static DataAffinity fromType(String type) {
        return computeAffinity(type);
    }

    /**
     * See http://www.sqlite.org/datatype3.html
     * section 2.1 Determination of column affinity
     */
    private static DataAffinity computeAffinity(String type) {
        String deftype = type.toLowerCase(Locale.US);
        if (deftype.contains("int")) {
            return DataAffinity.INTEGER;
        }
        if (containsOneOf(deftype, "char", "clob", "text")) {
            return DataAffinity.TEXT;
        }
        if (containsOneOf(deftype, "real", "floa", "doub")) {
            return DataAffinity.REAL;
        }
        if (containsOneOf(deftype, "blob") || deftype.equals("")) {
            return DataAffinity.NONE;
        }
        return DataAffinity.NUMERIC;
    }

    private static boolean containsOneOf(String toCheck, String... values) {
        for (String value : values) {
            if (toCheck.contains(value)) {
                return true;
            }
        }
        return false;
    }

}
