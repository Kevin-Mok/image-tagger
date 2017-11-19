# CSC207 — Java from the command line

As part of the project, you are required to provide file `help.txt`, which contains "a concise and clear set of instructions for how to compile and run your code from the command line". See the announcement from 5 November for more details.

We recommend that more than one group member try this, rather than having four people clustered around 1 computer.

## javac

In the Teaching Labs, `javac` compiles Java programs. It is one of the commands found on your path, which is a list of directories. To see your path, type this (what’s the separator character?):

    echo $PATH

To find out where `javac` is, type this (it’s probably something like `/local/bin/javac`):

    which javac

## The basics

Create an IntelliJ project. Create a package in the project called `pack`. Create a class called `Main` with a `main` method.

On the command line, use `cd` to change to the `src` directory in the project. When you type `ls`, you should see the package name.

Now type this; it should exit silently:

    javac pack/Main.java

That creates file `Main.class` inside directory `pack`. To run it:
  
    java pack.Main

## classpath

Sometimes you might use external libraries, which in Java come in files with suffix `.jar`. If you use one, you need to let the `javac` program know where it is. To do this, you will need to do some web research, maybe for something like "javac classpath example" (the word “example” can be of tremendous help in web searches). Let you TA know if you need help.

Make sure that the help instructions work for more than one group member!

## Back to the project

Spend the rest of the hour working on your project! Your TA will stop by to get a brief progress report.