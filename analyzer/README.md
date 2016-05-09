## sqlite-analyzer Gradle plugin source 

This project builds the sqlite-analyzer Gradle plugin.

### Note

This is an independent Gradle project. It is not part of the parent directory's multi-project build.

It is linked into the parent's demos via the `settings.gradle` in the parent's 
`buildSrc` project. This way, changes in the source code are picked up immediately
when re-running the parent's build, but still the parent project  and the plugin 
project can run on different Gradle and Java versions.
 
 
