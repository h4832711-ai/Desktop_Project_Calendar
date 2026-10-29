@echo off
cd /d "%~dp0"

echo Compiling Java files...

javac -encoding UTF-8 -d . date\*.java reminderAPI\*.java reminderController\*.java reminderModel\*.java reminderView\*.java ReminderApplication.java

if errorlevel 1 (
    echo Compilation failed.
    pause
    exit /b 1
)

echo Starting Desktop Calendar...
java -classpath . ReminderApplication

pause
