# sqlite-analyzer [![](http://ci.novoda.com/buildStatus/icon?job=sqlite-analyzer)](http://ci.novoda.com/job/sqlite-analyzer/lastBuild/console) [![](https://raw.githubusercontent.com/novoda/novoda/master/assets/btn_apache_lisence.png)](LICENSE.txt)

Code generation for Java/Android database access.

## Description

Generates java/android database access code by analysing sqlite migration files or sqlite databases, keeping full control of what code is generated.

sqlite-analyzer creates an in-memory sqlite database, either from a given database file or by running sql migrations, and analyzes its tables to construct a DatabaseModel. This model is then used to generate code.

This project uses [sqlite-jdbc](https://bitbucket.org/xerial/sqlite-jdbc) to create and analyze the database. [Groovy](http://groovy.codehaus.org/) is used to generate code, [Gradle](http://www.gradle.org/) to hook the functionality into the [android build system](http://tools.android.com/tech-docs/new-build-system).

## Adding to your project

To integrate sqliteAnalyzer into your project, it is recommended for now that you depend directly on the sources. See the setup under the
`buildSrc` sub-directory for an example.

If you prefer you can use it from jcenter directly, just add these lines to the build.gradle of your project:

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.novoda:sqlite-analyzer:0.0.1-beta'
    }
}
```

After you've added the sources to `buildSrc` or added the buildscript dependency, you can start using this library, add these lines to the `build.gradle` of your project:

```groovy
apply plugin: 'sqlite-analyzer'

sqliteAccess {
    migrationsDir 'src/main/assets/migrations'
    packageName 'com.novoda.sqliteprovider.demo.simple'
}
```


## Simple usage

Try `./gradlew clean assembleDebug` and observe the generated code under `build/source/sqlite/debug/`.
The project provides 3 demo applications that create and use database access code, using SqliteProvider, android-asset-helper and Google Auto resp.


## Links

Here are a list of useful links:

 * We always welcome people to contribute new features or bug fixes, [here is how](https://github.com/novoda/novoda/blob/master/CONTRIBUTING.md)
 * If you have a problem check the [Issues Page](https://github.com/novoda/sqlite-analyzer/issues) first to see if we are working on it
 * For further usage or to delve more deeply checkout the [Project Wiki](https://github.com/novoda/sqlite-analyzer/wiki)
 * Looking for community help, browse the already asked [Stack Overflow Questions](http://stackoverflow.com/questions/tagged/support-sqlite-analyzer) or use the tag: `support-sqlite-analyzer` when posting a new question
