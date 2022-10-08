#!/bin/bash

PROJECT_ROOT="."

SRC="$PROJECT_ROOT/src"
BUILD="$PROJECT_ROOT/build"
MANIFEST="$PROJECT_ROOT/META-INF/MANIFEST.MF"

ENTRY_PT="Main"
JAR_NAME="Lunar-Rocket-Simulator-LRS.jar"

mkdir -p "$BUILD"

if [ "$1" = "clean" ]; then
    rm -rf "$BUILD/"*.class  2> /dev/null
    rm -rf "$BUILD/res"      2> /dev/null
    exit 0
elif [ "$1" = "cleaner" ]; then
    rm -rf "$BUILD/"*.class  2> /dev/null
    rm -rf "$BUILD/res"      2> /dev/null
    rm "$JAR_NAME"           2> /dev/null
    exit 0
elif [ "$1" = "" ]; then
    javac -sourcepath "$SRC" -d "$BUILD" -h "$BUILD" $(find "$SRC/" -name "*.java")
    cp -r "$SRC/res" "$BUILD/"
    jar -cvmf "$MANIFEST" "$JAR_NAME" -C "$BUILD" $(find "$BUILD"/* | sed "s/build\///")
else
    echo "build.sh: invalid argument"
    exit 1
fi
