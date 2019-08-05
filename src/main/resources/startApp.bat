@ECHO OFF
rem #
rem # Copyright (c) 2019 Quinovas
rem #

echo "                                                                                                    "
echo "                                                                                                    "
echo "        _ __  __  _____  __          __     __  __ _____    _____                _     _            "
echo "       | |  \/  |/ ____| \ \        / /\   |  \/  |  __ \  |  __ \              (_)   | |           "
echo "       | | \  / | (___    \ \  /\  / /  \  | \  / | |__) | | |__) | __ _____   ___  __| | ___ _ __  "
echo "   _   | | |\/| |\___ \    \ \/  \/ / /\ \ | |\/| |  ___/  |  ___/ '__/ _ \ \ / / |/ _` |/ _ \ '__| "
echo "  | |__| | |  | |____) |    \  /\  / ____ \| |  | | |      | |   | | | (_) \ V /| | (_| |  __/ |    "
echo "   \____/|_|  |_|_____/      \/  \/_/    \_\_|  |_|_|      |_|   |_|  \___/ \_/ |_|\__,_|\___|_|    "
echo "                                                                                                    "
echo "                                                            version: 1.0-SNAPSHOT                   "

set "JMS_WAMP_PROVIDER_PATH=%cd%"

if not "%JMS_WAMP_PROVIDER_PATH%" == "" goto gotHome
if exist "%JMS_WAMP_PROVIDER_PATH%\startApp.bat" goto okHome

:gotHome
if exist "%JMS_WAMP_PROVIDER_PATH%\startApp.bat" goto okHome
    echo The JMS_WAMP_PROVIDER_PATH environment variable is not defined correctly
    echo This environment variable is needed to run this program
goto end

:okHome

rem Set JavaHome if it exists
if exist { "%JAVA_HOME%\bin\java" } (
    set JAVA="%JAVA_HOME%\bin\java"
)

echo Using JAVA_HOME:       "%JAVA_HOME%"
echo Using JMS_WAMP_PROVIDER_PATH:   "%JMS_WAMP_PROVIDER_PATH%"


rem create gc.log file
if not exist { "%JMS_WAMP_PROVIDER_PATH%\logs\gc.log" } (
    mkdir "%JMS_WAMP_PROVIDER_PATH%\logs" 2>nul
    type NUL > "%JMS_WAMP_PROVIDER_PATH%\logs\gc.log"
)

rem # This class has the main method to start the app
set MAIN_CLASS=com.echostreams.pulsar.mqtt.sender.PulsarMqttSender

set JAVA_OPTS=
set JAVA_OPTS_SCRIPT=-XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true
set JMS_WAMP_PROVIDER_PATH=%JMS_WAMP_PROVIDER_PATH%

rem # Use the Hotspot garbage-first collector.
set JAVA_OPTS=%JAVA_OPTS%  -XX:+UseG1GC

rem # Have the JVM do less remembered set work during STW, instead
rem # preferring concurrent GC. Reduces p99.9 latency.
set JAVA_OPTS=%JAVA_OPTS%  -XX:G1RSetUpdatingPauseTimePercent=5

rem # Main G1GC tunable: lowering the pause target will lower throughput and vise versa.
rem # 200ms is the JVM default and lowest viable setting
rem # 1000ms increases throughput. Keep it smaller than the timeouts.
set JAVA_OPTS=%JAVA_OPTS%  -XX:MaxGCPauseMillis=500

rem # Optional G1 Settings

rem  Save CPU time on large (>= 16GB) heaps by delaying region scanning
rem  until the heap is 70% full. The default in Hotspot 8u40 is 40%.
rem set JAVA_OPTS=%JAVA_OPTS%  -XX:InitiatingHeapOccupancyPercent=70

rem  For systems with > 8 cores, the default ParallelGCThreads is 5/8 the number of logical cores.
rem  Otherwise equal to the number of cores when 8 or less.
rem  Machines with > 10 cores should try setting these to <= full cores.
rem set JAVA_OPTS=%JAVA_OPTS%  -XX:ParallelGCThreads=16

rem  By default, ConcGCThreads is 1/4 of ParallelGCThreads.
rem  Setting both to the same value can reduce STW durations.
rem set JAVA_OPTS=%JAVA_OPTS%  -XX:ConcGCThreads=16

rem ## GC logging options -- uncomment to enable

set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintGCDetails
set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintGCDateStamps
set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintHeapAtGC
set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintTenuringDistribution
set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintGCApplicationStoppedTime
set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintPromotionFailure
rem set JAVA_OPTS=%JAVA_OPTS% -XX:PrintFLSStatistics=1
set JAVA_OPTS=%JAVA_OPTS% -Xloggc:%JMS_WAMP_PROVIDER_PATH%\logs\gc.log
set JAVA_OPTS=%JAVA_OPTS% -XX:+UseGCLogFileRotation
set JAVA_OPTS=%JAVA_OPTS% -XX:NumberOfGCLogFiles=10
set JAVA_OPTS=%JAVA_OPTS% -XX:GCLogFileSize=10M

%JAVA% -server %JAVA_OPTS% %JAVA_OPTS_SCRIPT% -cp %JMS_WAMP_PROVIDER_PATH%\lib\* %MAIN_CLASS%