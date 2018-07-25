#!/bin/bash  
# bash script that compiles all .java files passed as parameters
# and executes provided .class files

if [ -z $BASH_SOURCE ] ; then
    echo "--Script must be sourced (using '.' or 'source') and run under bash >= 3.0"
    exit 1
fi
CLASSPATH="" # construct classpath with .jar files in current folder
script_path="${BASH_SOURCE%/*}"
script_path_abs="$(cd $script_path ; pwd)"
jar_path="$script_path_abs"
for jarfile in "$jar_path"/*.jar ; do # find all .jar files in folder
        if [ -z "$CLASSPATH" ] ; then
            CLASSPATH="$jarfile"
        else   
            CLASSPATH="$jarfile:$CLASSPATH"
        fi
done
CLASSPATH="$CLASSPATH:." # add current folder
export CLASSPATH

javac -d . operations/*.java generator/*.java # compile all .java files
if [ $? -eq 0 ]; then
    echo "--Compilation completed."
    echo "-N          <number of surface points as a power of 2 (along square side)>"
    echo "-rL         <length of surface (along square side)>"
    echo "-h          <rms height>"
    echo "-clx (-cly) <correlation length in x (y)>"
    echo "-out        <output filename>"
    echo "[ENTER FLAGS]"
    read args 
    echo "--Executing Main.."
    java generator.Main $args
else
    echo "--Compilation failed."
    exit
fi

