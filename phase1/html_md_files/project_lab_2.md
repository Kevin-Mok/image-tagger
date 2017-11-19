# Project Lab 2 instructions

## Preamble: markdown

This text file uses markdown [1] formatting. It's strength is that markdown
syntax is about as simple as it gets for writing formatted documents, and there
are tools that produce web pages (and lots more) from them.

On the command line in this directory, run this command to create a webpage:

    multimarkdown -f -o project_lab_2.html project_lab_2.md

Once you've done that, open project_lab_2.html in a web browser. Because
project_lab_2.html is automatically geneated from another file in the repo,
don't add and commit it to your repo.

[1] [MultiMarkdown](http://fletcherpenney.net/multimarkdown/) is a version of
markdown that is installed in the Teaching Labs.

## Overview

These instructions are for the second project lab hour. There are two parts:

1. Create a list of design questions about the project.
2. Make a todo list.

## Design questions

The CRC Ticket Vendor lecture handout posted on the Labs page [2] has sample
CRC cards as well as a list of questions that helped you analyize and verify
the design. In this lab, you will come up with similar questions about the
project that your group will need to answer. These questions will serve as a
checklist for things you need to write code for.

Remember that you are designing the back end first and hooking up the GUI
later. [3] This will help you apply the Model-View-Controller pattern: you're
developing the Model, and perhaps parts of the Controller.

Read the ticket vendor handout questions to get ideas! You should spend a
couple minutes reviewing them before you start. They're on the last page.

Here are three example design questions. Read the project description to come
up with more.

* Which part(s) of the program are responsible for reading the contents of the
  starting directory?
* What happens when a new tag is created?
* Which class is keeping track of the association between tags and files?

In your repo is phase1/design_questions.txt. Edit it to add your own questions.
Add, commit, and push, of course.

[2] [Labs page](http://www.teach.cs.toronto.edu/~csc207h/fall/labs.shtml)

[3] Someone in your group will need to learn how to use the JavaFX framework at
some point and explain it to everyone else. This task should go on the todo
list that you'll develop after you finish the design questions.

## Todo list

In the last 15 minutes of the lab, make a todo list for the next two weeks.
Save this in your repo in phase1/todo.txt, then add, commit, and push.

As a git exercise, each group member - from their own account on the Teaching
Labs or from their laptop - should do a git pull, add their name to at least
one todo item, and push.

We will use `git shortlog` [4] to keep track of who is contributing, and you
can too.

[4] [git shortlog documentation](https://git-scm.com/docs/git-shortlog)
