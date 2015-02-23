package com.novoda.sqlite;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static com.novoda.sqlite.StringUtil.camelify;
import static com.novoda.sqlite.StringUtil.asCamelifyGetMethod;

public class StringUtilTest {

    @Test
    public void shouldCamelizeAllUppercaseUnderscoredColumn() {
        assertEquals("ColumnId", camelify("COLUMN_ID"));
    }

    @Test
    public void shouldCamelizeLowerUnderscoredColumn() {
        assertEquals("ColumnId", camelify("column_id"));
    }

    @Test
    public void shouldCamelizeMixedcaseColumn() {
        assertEquals("Columnid", camelify("columnID"));
    }

    @Test
    public void shouldCamelizeAllUppercaseColumn() {
        assertEquals("Columnid", camelify("COLUMNID"));
    }

    @Test
    public void shouldCamelizeGetterForAllUppercaseUnderscoredColumn() {
        assertEquals("getColumnId", asCamelifyGetMethod("COLUMN_ID"));
    }

    @Test
    public void shouldCamelizeGetterForAllLowercaseUnderscoredColumn() {
        assertEquals("getColumnId", asCamelifyGetMethod("column_id"));
    }

    @Test
    public void shouldCamelizeGetterForMixedcaseColumn() {
        assertEquals("getColumnid", asCamelifyGetMethod("columnID"));
    }

    @Test
    public void shouldCamelizeGetterForAllUppercaseColumn() {
        assertEquals("getColumnid", asCamelifyGetMethod("COLUMNID"));
    }
}
