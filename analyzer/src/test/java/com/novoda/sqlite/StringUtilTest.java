package com.novoda.sqlite;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilTest {
    @Test
    public void shouldCamelizeColumnNames(){
        assertEquals("ColumnId", StringUtil.camelify("COLUMN_ID"));
        assertEquals("Columnid", StringUtil.camelify("columnID"));
        assertEquals("Columnid", StringUtil.camelify("COLUMNID"));
    }

    @Test
    public void shouldCamelizeColumnNamesForMethods() {
        assertEquals("getColumnId", StringUtil.asCamelifyGetMethod("COLUMN_ID"));
        assertEquals("getColumnid", StringUtil.asCamelifyGetMethod("columnID"));
        assertEquals("getColumnid", StringUtil.asCamelifyGetMethod("COLUMNID"));
    }
}