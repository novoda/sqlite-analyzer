# SQLiteAnalyzer

### Purpose
This spike aims to generate java utility code from given sql migrations.

### Mechanics
We generate an in-memory sqlite database, run the migrations on it and analyze the resulting tables to construct a DatabaseModel.
This model is then used to generate code.

### Libraries
We use [sqlite-jdbc](https://bitbucket.org/xerial/sqlite-jdbc) to create and analyze the database.

### Integration
To integrate sqliteAnalyzer into your project, let your buildscript depend on the binary,
or see the setup under the `buildSrc` sub-directory for an example that directly links to the sources.

The code generation is then integrated with the gradle build via:

```groovy
/**
 * We register the DatabaseInfo generation task with all the variants as a java-generational task.
 * (Requires android gradle plugin >= 0.7.0)
 *
 * This makes the code available in the IDE.
 */
android.applicationVariants.all { variant ->
    // create a task that generates a java class
    File sourceFolder = file("${buildDir}/source/sqlite/${variant.dirName}")
    def javaGenerationTask = tasks.create(name: "generateSqlFor${variant.name.capitalize()}", type: tv.arte.plus7.sql.GenerateDatabaseInfo) {
        migrationsDir file("${projectDir.absolutePath}/src/main/assets/migrations")
        outputDir sourceFolder
        packageName 'com.novoda.sqliteprovider.demo.simple'
    }
    variant.registerJavaGeneratingTask(javaGenerationTask, sourceFolder)
}
```

License
-------

    (c) Copyright 2014 Novoda

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

