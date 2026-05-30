package dop.chapter01;

import java.util.UUID;

public class Listing1_4 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 1.4
   * ───────────────────────────────────────────────────────
   * Here's the magic of representation. I don't have to tell
   * you out of band what that ID is supposed to be. You don't
   * have to read an internal wiki, or ask a coworker, or look
   * inside the database. We can make the code itself communicate
   * what ID means by picking a better representation.
   * ───────────────────────────────────────────────────────
   */
  static class ImprovedRepresentation {
    UUID id;   //  ◄───┐ THIS tells us exactly what that ID should be! A UUID.
               //      │ Not an arbitrary string. Not a Product Code or SKU.
               //      │ ID is a UUID. Try to give it anything else and your
               //      │ code won't compile.
  }

}
