package dop.chapter12;

import dop.chapter12.Chapter12.Task.Completed;
import dop.chapter12.Chapter12.Task.Failed;
import dop.chapter12.Chapter12.Task.Started;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.lang.String.format;

public class Chapter12 {

  public record Item(Object object){}
  record Attr(String name){}
  sealed interface ConditionExpression {
    public record Eq(Attr attr, String value) implements ConditionExpression {}
    public record Exists(Attr attr) implements ConditionExpression {}
    public record In(Attr attr, Collection<String> values) implements ConditionExpression {}
    public record Not(ConditionExpression expr) implements ConditionExpression {}
    public record Or(ConditionExpression a, ConditionExpression b) implements ConditionExpression {}
    public record And(ConditionExpression a, ConditionExpression b) implements ConditionExpression {}
  }

  public static Attr attr(String value) {
    return new Attr(value);
  }
  public static ConditionExpression.Eq eq(Attr attr, String value){return new ConditionExpression.Eq(attr, value);}
  public static ConditionExpression.Exists exists(Attr attr) {return new ConditionExpression.Exists(attr);}
  public static ConditionExpression.In in(Attr attr, Collection<String> values) {return new ConditionExpression.In(attr, values);}
  public static ConditionExpression.Not not(ConditionExpression expr) {return new ConditionExpression.Not(expr);}
  public static ConditionExpression.Or or(ConditionExpression a, ConditionExpression b) {return new ConditionExpression.Or(a, b);}
  public static ConditionExpression.And and(ConditionExpression a, ConditionExpression b) {return new ConditionExpression.And(a, b);}

  static Item serialize(Object o) {
    return new Item(o);
  }

  static class ConditionFailedException extends RuntimeException {}


  interface SomeDistributedKeyValueStore {
    void putItem(Item item, ConditionExpression expression) throws ConditionFailedException;
  }

  record TaskId(String value){}
  sealed interface Task {
    public record Started(TaskId id /* other attrs */) implements Task {}
    public record Completed(TaskId id /* other attrs */) implements Task {}
    public record Failed(TaskId id /* other attrs */) implements Task {}
  }

  int order(Task task) {
    return switch (task) {
      case Started __   -> 0;
      case Completed __ -> 1;
      case Failed __    -> 1;
    };
  }

  int order(Optional<Task> job) {
    return job.map(this::order).orElse(-1);
  }

  boolean canTransitionTo(Task thisState, Task nextState) {
    return order(thisState) < order(nextState);
  }

  @Test
  void ordering() {
    List<Task> stuff = List.of(new Started(new TaskId("1")), new Completed(new TaskId("1")), new Failed(new TaskId("1")));

    for (Task a : stuff) {
      for (Task b : stuff) {
//        System.out.println(format("%s is < %s? %s", a.getClass().getSimpleName(), b.getClass().getSimpleName(), order.get(a.getClass()) < order.get(b.getClass())));
        System.out.println(format("// %s is < %s? %s", a.getClass().getSimpleName(), b.getClass().getSimpleName(), order(a) < order(b)));
      }
    }
  }

  static String tos(Optional<Task> o) {
    return o.isEmpty()
            ? "(Empty)"
            : format("%s", o.get().getClass().getSimpleName());
  }

  @Test
  void demoOrdering() {
    List<Optional<Task>> states = List.of(
      Optional.empty(),
      Optional.of(new Started(new TaskId("1"))),
      Optional.of(new Failed(new TaskId("1"))),
      Optional.of(new Completed(new TaskId("1")))
    );

    for (var a : states) {
      for (var b : states) {
        System.out.println(format("// %-10s can transition to %-10s %s", tos(a), tos(b), order(a) < order(b)));
      }
    }
  }

  SomeDistributedKeyValueStore db;

//  @Test
//  void onlyValidStateTransitionsAllowed() {
//    List<Optional<Task>> states = List.of(
//      Optional.empty(),
//      Optional.of(new Started(new TaskId("1"))),
//      Optional.of(new Failed(new TaskId("1"))),
//      Optional.of(new Completed(new TaskId("1")))
//    );
//
//    MyRepository repo = new MyRepository(db);
//    for (var a : states) {
//      for (var b : states) {
//        if (canTransitionTo(a, b)) {
//          Assertions.assertDoesNotThrow(() -> {
//            a.ifPresent(repo::save);
//            b.ifPresent(repo::save);
//          });
//        } else {
//          Assertions.assertThrows(ConditionFailedException.class, () -> {
//            a.ifPresent(repo::save);
//            b.ifPresent(repo::save);
//          });
//        }
//      }
//    }
//  }


  class MyRepository {
    private SomeDistributedKeyValueStore db;

    public MyRepository(SomeDistributedKeyValueStore db) {
      this.db = db;
    }

    public void save(Task task) throws ConditionFailedException {
      ConditionExpression condition = or(
        // all new writes are accepted regardless of state
        not(exists(attr("id"))),
        // However, each state is immutable. They can only transition.
        and(
          // The same state cannot be updated in place.
          eq(attr("state"), task.getClass().getSimpleName()),
          // they cannot transition out of a terminal state.
          not(in(attr("state"), List.of("Completed", "Failed")))
        )
      );
      db.putItem(serialize(task), condition);
    }
  }





}
