#!/bin/bash

PROJECT_ROOT="."

SRC="$PROJECT_ROOT/src"
BUILD="$PROJECT_ROOT/build"

ENTRY_PT="Main"
JAR_NAME="Lunar-Rocket-Simulator-LRS.jar"

mkdir -p "$BUILD"

if [ "$1" = "clean" ]; then
    rm -rf "$BUILD/"* 2> /dev/null
    exit 0
elif [ "$1" = "cleaner" ]; then
    rm -rf "$BUILD/"* 2> /dev/null
    rm "$JAR_NAME" 2> /dev/null
    exit 0
fi

javac -sourcepath "$SRC" -d "$BUILD" -h "$BUILD" $(find "$SRC/" -name "*.java")
cp -r "$SRC/res" "$BUILD/"
jar -e Main -cvf "$JAR_NAME" -C "$BUILD" $(find "$BUILD/"*)
