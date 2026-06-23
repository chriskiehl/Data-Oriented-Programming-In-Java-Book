package dop.chapter06;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class Listing6_08 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.8
   * ───────────────────────────────────────────────────────
   * Controlling for other parts of the system
   * ───────────────────────────────────────────────────────
   */
  @Test
  void testFigureOutDueDate() { // ◄── Our test is doomed to be devoted to setting up mocks
                                //     and stubs to control our dependencies

    //                      ┌─── We'll use Mockito throughout the book, but you could just as well
    //                      ▼    use custom interface implementations
    ContractsAPI mockApi = mock(ContractsAPI.class);
    lenient().when(mockApi.getRating()).thenReturn(PaymentTerms.NET_30);
//                              ▲
//                              └─ The worst part of having these in our function is
//                                 that we also have to write test to handle what
//                                 happens when they fail!
    LocalDate fixedDate = LocalDate.of(2024, 1, 1);
    try (MockedStatic<LocalDate> dateMock = mockStatic(LocalDate.class)) {
      dateMock.when(LocalDate::now).thenReturn(fixedDate);

      FeeProcessingService service = new FeeProcessingService(
          mockApi  //
          // plus some other object
          // and another...
          // and anything else we need...
      );

//    └──────────────────────────────────────────────────────────────────────┘
//                                ▲
//                                │
//                      Everything up here was
//                      just setting up the test!

      LocalDate result = service.figureOutDueDate();
      //     we call the thing under test

      Assertions.assertEquals("...", "...");
    }
  }















  interface ContractsAPI {
    PaymentTerms getRating();
  }

  enum PaymentTerms {NET_30}

  class FeeProcessingService {
    FeeProcessingService(ContractsAPI contractsAPI) {
    }

    LocalDate figureOutDueDate() {
      return null;
    }
  }

}
