package com.novoda.sqlite.generator

class JavaMapper {

    static String javaName(String name) {
        def accessor = javaAccessor(name)
        if (accessor.size() == 1)
            return accessor[0].toLowerCase()
        return accessor[0].toLowerCase() + accessor[1..-1]
    }

    static String javaAccessor(String name) {
        assert name.size() > 0
        return camelize(javaCompliant(name))
    }

    private static String camelize(String compliant) {
        def builder = new StringBuilder()
        compliant.split("_").each {
            builder.append(it.toLowerCase().capitalize())
        }
        builder.toString()
    }

    private static String javaCompliant(String name) {
        char[] chars = name.chars
        char[] javaChars = new char[chars.length]
        javaChars[0] = Character.isJavaIdentifierStart(chars[0]) ? chars[0] : '_'
        for (int i = 1; i < javaChars.length; i++) {
            javaChars[i] = Character.isJavaIdentifierPart(chars[i]) ? chars[i] : '_'
        }
        new String(javaChars)
    }
}

