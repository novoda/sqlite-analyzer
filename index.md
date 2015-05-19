---
layout: page
---
## Description

Generates java/android database access code by analysing sqlite migration files or sqlite databases,
keeping full control of what code is generated.

sqlite-analyzer creates an in-memory sqlite database, either from a given database file or by reading sql migrations,
and analyzes its tables to construct a DatabaseModel. This model is then used to generate database access code.

This project uses [sqlite-jdbc](https://bitbucket.org/xerial/sqlite-jdbc) to create and analyze the database.
[Groovy](http://groovy.codehaus.org/) is used to generate code, [Gradle](http://www.gradle.org/) to hook
the functionality into the [android build system](http://tools.android.com/tech-docs/new-build-system).

## Adding to your project

To integrate sqlite-analyzer into your project, add the following at the beginning of the `build.gradle` of your project:

``` groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.novoda:sqlite-analyzer:0.3.1'
    }
}
```

To use the library with [sqlite-provider](https://github.com/novoda/sqlite-provider), add these lines to the `build.gradle` of your project:

```
apply plugin: 'sqlite-analyzer'
sqliteAccess {
    migrationsDir 'src/main/assets/migrations'
    packageName 'com.novoda.sqliteprovider.demo.simple'
}
```

See the sample projects for setup with [android-sqlite-asset-helper](https://github.com/jgilfelt/android-sqlite-asset-helper).


## Simple usage

Try `./gradlew clean assembleDebug` and observe the generated code under `build/generated/source/sqlite/debug/`.

By default, it contains one single class `DB` that defines constants for the names of the tables and columns and
introduces static accessor methods as well as model classes for all tables data.

The project comes with 2 demo applications that create and use database access code,
one uses [sqlite-provider](https://github.com/novoda/sqlite-provider),
the other uses [android-sqlite-asset-helper](https://github.com/jgilfelt/android-sqlite-asset-helper).

**Use column names**
```
    queryBuilder.appendWhere(DB.Columns.Shop.Name + " like 'A%'")
```

Simplified use with static import 
```
    projection = new String[] {Employees.Firstname, Employees.Lastname};
```

**Use model classes**
```
public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    DB.Shop shop = DB.Shop.fromCursor(cursor);
}
```

**Use helpers for ContentValues**
```
   ContentValues values = new ContentValues(1);
   DB.Shop.setName("New Shop", values);
```

## Links

[![](https://ci.novoda.com/buildStatus/icon?job={{site.project.name}})](https://ci.novoda.com/job/{{site.project.name}}/lastBuild/console)
[![](https://raw.githubusercontent.com/novoda/novoda/master/assets/btn_apache_lisence.png)](https://github.com/novoda/{{site.project.name}}/LICENSE.txt)

Here are a list of useful links:

 * We always welcome people to contribute new features or bug fixes, [here is how](https://github.com/novoda/novoda/blob/master/CONTRIBUTING.md)
 * If you have a problem check the [Issues Page](https://github.com/novoda/{{site.project.name}}/issues) first to see if we are working on it
 * For further usage or to delve more deeply checkout the [Project Wiki](https://github.com/novoda/{{site.project.name}}/wiki)
 * Looking for community help, browse the already asked [Stack Overflow Questions](http://stackoverflow.com/questions/tagged/support-{{site.project.name}}) or use the tag: `support-{{site.project.name}}` when posting a new question
