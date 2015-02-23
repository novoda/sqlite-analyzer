package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import groovy.text.GStringTemplateEngine

class GetColumnFromCursorGenerator {

    private static final String TEMPLATE = '''\
public static $returnType $prefix$methodName(android.database.Cursor cursor) {
    int index = cursor.getColumnIndexOrThrow("$rowName");\
<% if(nullable) {%>
    if (cursor.isNull(index)) {
        return null;
    }\
<% } %>
    return cursor.get$cursorFunction(index)<% if (isBool) out << " > 0" %>;
}
'''
    private final Column column

    GetColumnFromCursorGenerator(Column column) {
        this.column = column
    }

    String print() {
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([returnType: column.dataType,
                methodName: column.camelizedName,
                rowName: column.name,
                cursorFunction: column.cursorAccessor,
                nullable: column.nullable,
                isBool: column.boolean,
                prefix: column.getterPrefix])
                .toString()
    }
}