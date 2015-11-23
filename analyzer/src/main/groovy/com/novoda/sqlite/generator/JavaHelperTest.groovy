import com.novoda.sqlite.generator.JavaHelper

def assertJava(source, accessor, name) {
  JavaHelper.with {
    assert javaAccessor(source) == accessor
    assert javaName(source) == name
  }
}

assertJava "hello_column", "HelloColumn", "helloColumn"
assertJava "_column", "Column", "column"
assertJava "hälööö", "Hälööö", "hälööö"
assertJava "id:1", "Id1", "id1"
assertJava "PRIME-CO", "PrimeCo", "primeCo"
assertJava "Ä", "Ä", "ä"



