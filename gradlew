#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
APP_HOME=$(cd "$(dirname "$0")"; pwd)

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS=""

# Locate the Java executable
if [ -n "$JAVA_HOME" ] ; then
    JAVA_EXE="$JAVA_HOME/bin/java"
else
    JAVA_EXE="java"
fi

# Build the classpath for Gradle 9 wrapper
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper-main-9.7.1.jar:$APP_HOME/gradle/wrapper/gradle-wrapper-shared-9.7.1.jar"

# Execute Gradle
exec "$JAVA_EXE" $DEFAULT_JVM_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
