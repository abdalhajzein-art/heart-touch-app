#!/usr/bin/env sh

APP_HOME=$(cd "$(dirname "$0")"; pwd)

DEFAULT_JVM_OPTS=""

if [ -n "$JAVA_HOME" ] ; then
    JAVA_EXE="$JAVA_HOME/bin/java"
else
    JAVA_EXE="java"
fi

# Gradle 9 wrapper classpath
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper-main-9.7.1.jar:$APP_HOME/gradle/wrapper/gradle-wrapper-shared-9.7.1.jar"

# Run Gradle 9 wrapper
exec "$JAVA_EXE" $DEFAULT_JVM_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
