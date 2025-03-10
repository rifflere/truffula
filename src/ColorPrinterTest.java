import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ColorPrinterTest {

  @Test
  void testPrintlnWithRedColorAndReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.RED);

    // Act: Print the message
    String message = "I speak for the trees";
    printer.println(message);


    String expectedOutput = ConsoleColor.RED + "I speak for the trees" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintWithRedColorAndReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.RED);

    // Act: Print the message
    String message = "I speak for the trees";
    printer.print(message);


    String expectedOutput = ConsoleColor.RED + "I speak for the trees" + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testWithColorChange() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLUE);

    // Act: Print the messages
    String message1 = "I speak for the trees";
    printer.print(message1);

    printer.setCurrentColor(ConsoleColor.GREEN);
    String message2 = "I love Binary trees!";
    printer.print(message2);

    String expectedOutput = ConsoleColor.BLUE + "I speak for the trees" + ConsoleColor.RESET + ConsoleColor.GREEN + "I love Binary trees!" + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintRedResetYellowNoResetYellow() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    // Act: Print the messages
    printer.setCurrentColor(ConsoleColor.RED);
    String message1 = "Apples!";
    printer.print(message1);

    printer.setCurrentColor(ConsoleColor.YELLOW);
    String message2 = "Bananas!";
    printer.print(message2, false);

    String message3 = "Pineapples!";
    printer.print(message3);

    String expectedOutput = ConsoleColor.RED + "Apples!" + ConsoleColor.RESET + ConsoleColor.YELLOW + "Bananas!" + ConsoleColor.YELLOW + "Pineapples!" + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintNullInput() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    String message = null;

    assertThrows(IllegalArgumentException.class, () -> {
      printer.print(message);
    }, "Illegal Argument Exception Expected");
  }

  @Test
  void testPrintEmptyString() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    // Act: Print the message
    String message = "";
    printer.print(message);

    String expectedOutput = "";

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }
}
