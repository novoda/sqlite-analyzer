# SQLiteAnalyzer
Caution: This project is still in an early alpha stage.

### Purpose
Generate java/android database access code by analyzing sqlite migration files or sqlite databases, keeping full control of what
code is generated.

### Integration
The sqliteAnalyzer comes as a gradle plugin relying on the android gradle plugin.

To integrate sqliteAnalyzer into your project, we currently recommend depending directly on the sources.
See the setup under the `buildSrc` sub-directory for an example.

The code generation is then integrated with the gradle build via:

```groovy
apply plugin: 'sqlite-access'

sqliteAccess {
    migrationsDir 'src/main/assets/migrations'
    packageName 'com.novoda.sqliteprovider.demo.simple'
}
```

Try `./gradlew clean assembleDebug` and observe the generated code under `build/source/sqlite/debug/`.

### Demos
The project provides 3 demo applications that create and use database access code, using SqliteProvider, android-asset-helper and Google Auto resp.

### Mechanics
We create an in-memory sqlite database, either from a given database file or by running sql migrations, and analyze its tables to construct a DatabaseModel.
This model is then used to generate code.

### Libraries
We use [sqlite-jdbc](https://bitbucket.org/xerial/sqlite-jdbc) to create and analyze the database.
[Groovy](http://groovy.codehaus.org/) is used to generate code, [Gradle](http://www.gradle.org/) to hook the functionality into the [android build system](http://tools.android.com/tech-docs/new-build-system).

### Releases
There are no releases yet.


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

