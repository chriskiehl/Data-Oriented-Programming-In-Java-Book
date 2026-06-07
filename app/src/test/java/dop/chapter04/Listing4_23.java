package dop.chapter04;

import java.time.Instant;
import java.util.List;

public class Listing4_23 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 4.23
   * ───────────────────────────────────────────────────────
   * let's take a few steps back and rethink the design.
   * Here's where we'll start.
   * ───────────────────────────────────────────────────────
   */
  // All defined in previous listings
  record User(String name) {}
  record Step(String name) {}
  record Template(String name, List<Step> steps) {}
  record Instance(String name, Instant date, Template template) {}
  enum State {NOT_STARTED, COMPLETED, SKIPPED}
  record Status(
      Template template,
      Step step,
      boolean isCompleted,
      User completedBy,
      Instant completedOn
   ) {}

}
