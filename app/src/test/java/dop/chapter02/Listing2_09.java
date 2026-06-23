package dop.chapter02;

import java.util.ArrayList;
import java.util.List;

public class Listing2_09 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.9
   * ───────────────────────────────────────────────────────
   * Heads up! This listing looks very different from the one
   * in the book. Unlike plain text on a page, which doesn't
   * need to compile, Java does need that text to be compilable.
   * So, we have to do a lot of interface shimming in order to
   * get the (mostly) made up API in the example to compile.
   * ───────────────────────────────────────────────────────
   */
  public List<Customer> example() {
    // Here's the bulk of the listing. It's exploring how we often
    // use the Java collections as Identity Objects rather than
    // value objects.
    List<Customer> customers = new ArrayList<>(); // ◄─────  Creating the collection gives us a
                                                  //         useful identity we can carry around
                                                  //         as we do work

    QueryResponse response = database.query(request);
    for (Result result : response.items()) {
      customers.add(new Customer(result));  //       ◄─────  Which we do here
    }

    while (response.nextToken() != null) {
      response = database.query(request, response.nextToken());
      for (Result result : response.items()) {
        customers.add(new Customer(result));  //     ◄─────  And then over-and-over again down here
      }
    }
    return customers;  //  ◄───────────────────────────────  Before finally returning it here
  }





  // This section just sets up a bunch of fake interfaces
  // for our "API". It's ignorable.
  // ────────────────────────────────────────────────────────┐
  interface Result {}

  interface QueryResponse {                                //│
    List<Result> items();                                  //│
    String nextToken();                                    //│
  }

  //                                                         │
  interface DefinitelyNotDynamoDb {                        //│
    QueryResponse query(String request);                   //│
    QueryResponse query(String request, String nextToken); //│
  }

  class Customer {
    Customer(Result result) {}
  };


  // Ditto here.
  // These are just placeholders for compilation
  String request = null;
  DefinitelyNotDynamoDb database = null;

}
