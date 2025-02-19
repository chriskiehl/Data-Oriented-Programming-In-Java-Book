# Data Oriented Programming in Java

Source code for the book Data Oriented Programming in Java (by me! Chris Kiehl!)

<p align="center">
    <img src="https://freecontent.manning.com/wp-content/uploads/DOTD_NewMEAP_Kiehl.png" />
</p>

* [Get the book here!](https://mng.bz/BgQv)
* ISBN: 9781633436930

> [!Note]
> This book is in Early Access while I continue to work on it. This repository will be updated as new chapters are released.

This book is a distillation of everything I’ve learned about what effective development looks like in Java (so far!). It’s what’s left over after years of experimenting, getting things wrong (often catastrophically), and
slowly having anything resembling “devotion to a single paradigm” beat out of me by the great humbling filter that is reality.

Data-orientation doesn't replace object orientation. The two work together and enhance each other. DoP is born from a very simple idea, and one that people have been repeatedly rediscovering since the dawn of computing: “representation is the essence of programming”. Programs that are organized around the data they manage tend to be simpler, smaller, and significantly easier understand. When we do a really good job of capturing the data in our domain, the rest of the system tends to fall into place in a way which can feel like it’s writing itself.

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

### Running the code

The `tests/` package houses all of the runnable code. You can run all the tests in a class with this command:

```
gradle test --tests 'path.to.test.Class'
```
e.g.
```
gradle test --tests 'dop.chapter02.Listings'
```

You can also run individual tests by specifying the method.

```
gradle test --tests 'dop.chapter02.Listings.listing_2_1'
```



### How to use this repository

Each chapter in the book has an associated package in the `src/test/` directory. Most of the examples aren't necessarily things that we'll run. They're primarily for study. We'll look at them and go "Hmm. Interesting." DoP is a book that's about design decisions and how they affect our code. We're striving to make incorrect states impossible to express or compile. Thus, a lot of the examples are exploring how code changes (or _disappears_ entirely) when we get our modeling right.

**Listings in the Book vs Code**

Each listing in the book will have a corresponding example in the code. The Javadoc will describe which listing the code applies to.

```
/**
* ───────────────────────────────────────────────────────
*                      Listing 1.1
* ───────────────────────────────────────────────────────
*
* Here's an example of how we might traditionally model
* data "as data" using a Java object.
* ───────────────────────────────────────────────────────
*/
```

Sometimes, separate listings in the book will be combined into one example in the code.

```
/**
 * ───────────────────────────────────────────────────────
 *                Listings 1.5 through 1.9
 * ───────────────────────────────────────────────────────
 * Representation affects our ability to understand the code
 * as a whole. [...]
 * ───────────────────────────────────────────────────────
 */
```

> [!Note]
> The class names in the code will often differ from the class names used in the book. Java doesn't let us redefine classes over and over again (which we do in the book as we refactor), so we 'cheat' by appending a qualifying suffix. For instance, `ScheduledTask` in listing A might become `ScheduledTaskV2` or `ScheduledTaskWithBetterOOP` in a subsequent example code. The listing numbers in the Javadoc will always tie to the Listing numbers in the book.


**Character Encodings**

Make sure your IDE / Text editor is configured for UTF-8 character encoding (Windows tends to default to other encodings). Many of the in-code diagrams leverage the utf-8 charset.

Example utf-8 diagram:
```
// An informational black hole!
//
//  ┌────────  It returns nothing!
//  ▼
// void reschedule( ) {   //  ◄─────────────────────────────────┐
//     ...         ▲                                            │ Compare how very different
// }               └────── It takes nothing!                    │ these two methods are in
//                                                              │ terms of what they convey
RetryDecision reschedule(FailedTask failedTask) {       //  ◄───┘ to us as readers
    // ...
}
```


**Classes inside of methods**

> [!Note]
> Some listings will have classes defined in the body of the listing. These are only there because Java doesn't allow us to define records with forward references directly in a method's body, whereas doing it inside a class is fine. Thus, the kinda wonky occasional pattern of class(method(class (record, record, record, etc.)))


```
class Chapter05 {
    void listing5_13() {
        class __ {   ◄─────────────────────────────────┐ This nested class is here becuase Java doesn't support  
                                                       │ certain forward references / type hierarchies if you try  
            // ...                                     │ to define them directly in the body of the method. 
            // rest of example code                    │ Wrapping all the definitions in their own class allows 
            // ...                                     │ things to (mostly) behave as expected. 
        }
    }
}
```


## Table of Contents

| Chapter                                                   | Code Listings                                                                                                                                 | 
|-----------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| Chapter 01 - Data Oriented Programming                    | [Listings.java](https://github.com/chriskiehl/Data-Oriented-Programming-In-Java-Book/blob/main/app/src/test/java/dop/chapter01/Listings.java) |
| Chapter 02 - Data, Identity, and Values                   | [Listings.java](https://github.com/chriskiehl/Data-Oriented-Programming-In-Java-Book/blob/main/app/src/test/java/dop/chapter02/Listings.java) |
| Chapter 03 - Data and Meaning                             | [Listings.java](https://github.com/chriskiehl/Data-Oriented-Programming-In-Java-Book/blob/main/app/src/test/java/dop/chapter03/Listings.java) |
| Chapter 04 - Representation is the Essence of Programming | [Listings.java](https://github.com/chriskiehl/Data-Oriented-Programming-In-Java-Book/blob/main/app/src/test/java/dop/chapter04/Listings.java) |
| Chapter 05 - Modeling Domain Behaviors                    | [Listings.java](https://github.com/chriskiehl/Data-Oriented-Programming-In-Java-Book/blob/main/app/src/test/java/dop/chapter05/Listings.java) |
| Chapter 06 - Implementing the domain model                | [Listings.java](https://github.com/chriskiehl/Data-Oriented-Programming-In-Java-Book/blob/main/app/src/test/java/dop/chapter06/Listings.java) |
| Chapter 07 - Guiding the design with properties           | Coming soon!                                                                                                                                  |


## Questions and Feedback

I'd love to hear any and all feedback. You can leave comments in the [Manning forum](https://livebook.manning.com/forum?product=kiehl&page=1). I'm also very responsive to emails. If you have a question about the repo, feel free to write me at me@chriskiehl.com. 





