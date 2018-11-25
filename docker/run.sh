#!/bin/sh
gcdir="/var/log/data/gc"
if [ ! -d "$gcdir" ]; then
 mkdir -p $gcdir
fi

heapConfig="-Xms256m -Xmx256m -XX:+UseG1GC -XX:MaxGCPauseMillis=100"
metaspaceConfig="-XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=1048M"
logGcConfig="-Xloggc:$gcdir/gc-$(date +%Y%m%d-%H%M%S).log -XX:+PrintGC -XX:-PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=2 -XX:GCLogFileSize=16M"

JAVA_OPTS="$JAVA_OPTS $heapConfig -XX:+HeapDumpOnOutOfMemoryError $metaspaceConfig $logGcConfig"
export JAVA_OPTS

# For production
java $JAVA_OPTS -jar /var/data/service.jar

# For remote debugging (disabled by default)
#/usr/java/latest/bin/java $JAVA_OPTS -jar -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000" /var/data/service.jar --spring.profiles.active=production
