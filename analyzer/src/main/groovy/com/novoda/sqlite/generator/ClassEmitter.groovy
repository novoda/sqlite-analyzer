package com.novoda.sqlite.generator
import com.novoda.sqlite.generator.model.Access
import groovy.text.GStringTemplateEngine

class ClassEmitter {

    Access access
    File baseDir
    String packageName
    String className

    private final GStringTemplateEngine engine = new GStringTemplateEngine()

    public void print() {
        def url = TABLE_TEMPLATE // NewDBPrinter.class.getResource('/templates/DBAccess.template') //
        def targetDir = makeFileDir()
        new FileWriter("${targetDir}/${className}.java").withWriter { Writer writer ->
            template(url).make(access: access, packageName: packageName, className: className).writeTo(writer)
        }
    }

    private template(def text) {
        engine.createTemplate(text)
    }

    private String makeFileDir() {
        String packageAsDir = packageName.replaceAll(~/\./, "/")
        def fileDir = new File(baseDir, packageAsDir)
        fileDir.mkdirs()
        return fileDir.absolutePath
    }


    private static final String TABLE_TEMPLATE = '''\
package $packageName;

public final class $className {
public static final class Tables {
<% access.tables.each { dataSet -> %>\
    public static final String $dataSet.name = "$dataSet.sqlName";
<% } %>\
}

public static final class Columns {
<% access.tables.each { dataSet -> %>\
    public static final class $dataSet.name {
<%      dataSet.fields.each { field -> %>\
        public static final String $field.accessor = "$field.sqlName";
<%      } %>\
    }
<% } %>\
}

<% access.tables.each { dataSet -> %>\
public static final class $dataSet.name {
<% dataSet.fields.each { field -> %>\
    public static $field.type $field.getMethod(android.database.Cursor cursor) {
        int index = cursor.getColumnIndexOrThrow("$field.sqlName");
<% if(field.optional) {%>\
        if (cursor.isNull(index)) {
            return null;
        }
<% } %>\
        return cursor.get$field.cursorType(index)<% if (field.type ==~ /^(?i)boolean$/) out << " > 0" %>;
    }
    public static void $field.setMethod($field.type value, android.content.ContentValues values) {
        values.put("$field.sqlName", value);
    }
<% } %>\

<% dataSet.fields.each { field -> %>\
    private final $field.type $field.name;
<% } %>\

    public $dataSet.name(\
<% dataSet.fields.eachWithIndex { field, index ->
    out << "$field.type $field.name"
    if (index < dataSet.fields.size()-1)
      out << ", "
} %>) {
<% dataSet.fields.each { field -> %>\
        this.$field.name = $field.name;
<% } %>\
    }

<% dataSet.fields.each { field -> %>\
    public final $field.type $field.getMethod() {
        return $field.name;
    }
<% } %>\
}
<% } %>\
}
'''
}
