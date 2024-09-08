# Data Oriented Programming in Java

Source code for the book Data Oriented Programming in Java (by me! Chris Kiehl!)

<p align="center">
    <img src="https://github.com/chriskiehl/Data-Oriented-Programming-In-Java-Book/blob/main/graphics/book-cover.JPG?raw=true" />
</p>

* ISBN 

> [!Heads up!]
> This book is still in progress. This repository will be updated as new chapters are released. 

This book is a distillation of everything I’ve learned about what effective development looks like in Java (so far!). It’s what’s left over after years of experimenting, getting things wrong (often catastrophically), and
slowly having anything resembling “devotion to a single paradigm” beat out of me by the great humbling filter that is reality.

Data-orientation is born from a very simple idea, and one that people have been repeatedly rediscovering since the dawn of computing: “representation is essence of programming”. Programs that are organized around the data they manage tend to be simpler, smaller, and significantly easier understand. When we do a really good job of capturing the data in our domain, the rest of the system tends to fall into place in a way which can feel like it’s writing itself.

## Getting Started with this project

To download a copy of this repository, click on the [Download ZIP](https://github.com/chriskiehl/Data-Oriented-Programming-In-Java-Book/archive/refs/heads/main.zip) button or execute the following command in your terminal:

```
git clone https://github.com/chriskiehl/Data-Oriented-Programming-In-Java-Book.git
```

(If you downloaded the code bundle from the Manning website, please consider visiting the official code repository on GitHub at https://github.com/chriskiehl/Data-Oriented-Programming-In-Java-Book for the latest updates.)

The project is built with [Gradle](https://gradle.org/).  

```
gradle build
```

The `tests/` package houses any runnable code. You can run all the tests in a class with this command:

```
gradle test --tests 'path.to.test.Class'
```
e.g. 
```
gradle test --tests 'dop.chapter01.Chapter01Test'  
```

You can also run individual tests by specifying the method. 

```
gradle test --tests 'dop.chapter01.Chapter01Test.exampleMethod'
```



## How to use this repository

Each chapter in the book has an associated package in the `src/` directory. Most of the examples aren't neccessarily things that we'll run. They're there for study. DoP is a book that's about design decisions and how they affect our code. We're striving to make incorrect states impossible to express or compile. Thus, a lot of the examples are exploring how our representation of the code changes as we refine our data model.  

> [!Note]
> Make sure your IDE / Text editor is configured for UTF-8 character encoding (Windows tends to default to other encodings). Many of the in-code diagrams leverage the utf-8 charset. 



## Questions and Feedback

I'm most responsive to emails. If you have a question about the repo, feel free to write me at me@chriskiehl.com. 





