import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TruffulaPrinterTest {

    // order
    // color on hidden on - y
    // color off hidden on - y
    // color on hidden off - y
    // color off hidden off - y
    // root directory is hidden
    // root directoy is null - y
    // super deep directory

    @Test
    public void printTreeColorTrueHiddenTrue(@TempDir File tempDir) throws IOException {
        // Sample Directory:
        //
        // folder/
        //  image.png
        //  nested-folder/
        //      .hidden-folder/
        //          42.png
        //      .hidden.txt
        //      not-hidden.txt
        //  text.txt

        // Create folders
        File folder = new File(tempDir, "folder");
        folder.mkdir();
        File nestedFolder = new File(folder, "nested-folder");
        nestedFolder.mkdir();
        File hiddenFolder = new File(nestedFolder, ".hidden-folder");
        hiddenFolder.mkdir();

        // Create files
        File image = new File(folder, "image.png");
        File fortyTwo = new File(hiddenFolder, "42.png");
        File text = new File(folder, "text.txt");
        File notHidden = new File(nestedFolder, "not-hidden.txt");
        File hidden = new File(nestedFolder, ".hidden.txt");
        image.createNewFile();
        fortyTwo.createNewFile();
        text.createNewFile();
        notHidden.createNewFile();
        hidden.createNewFile();

        // set hidden files (for Windows)
        Path path = Paths.get(hiddenFolder.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        path = Paths.get(hidden.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        assertTrue(hiddenFolder.isHidden());
        assertTrue(hidden.isHidden());

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(folder, true, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        // Sample Directory:
        //
        // folder/
        //  image.png
        //  nested-folder/
        //      .hidden-folder/
        //          42.png
        //      .hidden.txt
        //      not-hidden.txt
        //  text.txt

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("folder/").append(nl).append(reset);
        expected.append(purple).append("   image.png").append(nl).append(reset);
        expected.append(purple).append("   nested-folder/").append(nl).append(reset);
        expected.append(yellow).append("      .hidden-folder/").append(nl).append(reset);
        expected.append(white).append("         42.png").append(nl).append(reset);
        expected.append(yellow).append("      .hidden.txt").append(nl).append(reset);
        expected.append(yellow).append("      not-hidden.txt").append(nl).append(reset);
        expected.append(purple).append("   text.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void printTreeColorFalseHiddenTrue(@TempDir File tempDir) throws IOException {
        // Sample Directory:
        //
        // folder/
        //  image.png
        //  nested-folder/
        //      .hidden-folder/
        //          42.png
        //      .hidden.txt
        //      not-hidden.txt
        //  text.txt

        // Create folders
        File folder = new File(tempDir, "folder");
        folder.mkdir();
        File nestedFolder = new File(folder, "nested-folder");
        nestedFolder.mkdir();
        File hiddenFolder = new File(nestedFolder, ".hidden-folder");
        hiddenFolder.mkdir();

        // Create files
        File image = new File(folder, "image.png");
        File fortyTwo = new File(hiddenFolder, "42.png");
        File text = new File(folder, "text.txt");
        File notHidden = new File(nestedFolder, "not-hidden.txt");
        File hidden = new File(nestedFolder, ".hidden.txt");
        image.createNewFile();
        fortyTwo.createNewFile();
        text.createNewFile();
        notHidden.createNewFile();
        hidden.createNewFile();

        // set hidden files (for Windows)
        Path path = Paths.get(hiddenFolder.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        path = Paths.get(hidden.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        assertTrue(hiddenFolder.isHidden());
        assertTrue(hidden.isHidden());

        // Set up TruffulaOptions with showHidden = true and useColor = false
        TruffulaOptions options = new TruffulaOptions(folder, true, false);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        // String purple = "\033[0;35m";
        // String yellow = "\033[0;33m";

        // Sample Directory:
        //
        // folder/
        //  image.png
        //  nested-folder/
        //      .hidden-folder/
        //          42.png
        //      .hidden.txt
        //      not-hidden.txt
        //  text.txt

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("folder/").append(nl).append(reset);
        expected.append(white).append("   image.png").append(nl).append(reset);
        expected.append(white).append("   nested-folder/").append(nl).append(reset);
        expected.append(white).append("      .hidden-folder/").append(nl).append(reset);
        expected.append(white).append("         42.png").append(nl).append(reset);
        expected.append(white).append("      .hidden.txt").append(nl).append(reset);
        expected.append(white).append("      not-hidden.txt").append(nl).append(reset);
        expected.append(white).append("   text.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void printTreeColorTrueHiddenFalse(@TempDir File tempDir) throws IOException {
        // Sample Directory:
        //
        // folder/
        //  image.png
        //  nested-folder/
        //      .hidden-folder/
        //          42.png
        //      .hidden.txt
        //      not-hidden.txt
        //  text.txt

        // Create folders
        File folder = new File(tempDir, "folder");
        folder.mkdir();
        File nestedFolder = new File(folder, "nested-folder");
        nestedFolder.mkdir();
        File hiddenFolder = new File(nestedFolder, ".hidden-folder");
        hiddenFolder.mkdir();

        // Create files
        File image = new File(folder, "image.png");
        File fortyTwo = new File(hiddenFolder, "42.png");
        File text = new File(folder, "text.txt");
        File notHidden = new File(nestedFolder, "not-hidden.txt");
        File hidden = new File(nestedFolder, ".hidden.txt");
        image.createNewFile();
        fortyTwo.createNewFile();
        text.createNewFile();
        notHidden.createNewFile();
        hidden.createNewFile();

        // set hidden files (for Windows)
        Path path = Paths.get(hiddenFolder.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        path = Paths.get(hidden.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        assertTrue(hiddenFolder.isHidden());
        assertTrue(hidden.isHidden());

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(folder, false, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        // Sample Directory:
        //
        // folder/
        //  image.png
        //  nested-folder/
        //      .hidden-folder/
        //          42.png
        //      .hidden.txt
        //      not-hidden.txt
        //  text.txt

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("folder/").append(nl).append(reset);
        expected.append(purple).append("   image.png").append(nl).append(reset);
        expected.append(purple).append("   nested-folder/").append(nl).append(reset);
        expected.append(yellow).append("      not-hidden.txt").append(nl).append(reset);
        expected.append(purple).append("   text.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void printTreeColorFalseHiddenFalse(@TempDir File tempDir) throws IOException {
        // Sample Directory:
        //
        // folder/
        //  image.png
        //  nested-folder/
        //      .hidden-folder/
        //          42.png
        //      .hidden.txt
        //      not-hidden.txt
        //  text.txt

        // Create folders
        File folder = new File(tempDir, "folder");
        folder.mkdir();
        File nestedFolder = new File(folder, "nested-folder");
        nestedFolder.mkdir();
        File hiddenFolder = new File(nestedFolder, ".hidden-folder");
        hiddenFolder.mkdir();

        // Create files
        File image = new File(folder, "image.png");
        File fortyTwo = new File(hiddenFolder, "42.png");
        File text = new File(folder, "text.txt");
        File notHidden = new File(nestedFolder, "not-hidden.txt");
        File hidden = new File(nestedFolder, ".hidden.txt");
        image.createNewFile();
        fortyTwo.createNewFile();
        text.createNewFile();
        notHidden.createNewFile();
        hidden.createNewFile();

        // set hidden files (for Windows)
        Path path = Paths.get(hiddenFolder.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        path = Paths.get(hidden.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        assertTrue(hiddenFolder.isHidden());
        assertTrue(hidden.isHidden());

        // Set up TruffulaOptions with showHidden = false and useColor = false
        TruffulaOptions options = new TruffulaOptions(folder, false, false);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        // String purple = "\033[0;35m";
        // String yellow = "\033[0;33m";

        // Sample Directory:
        //
        // folder/
        //  image.png
        //  nested-folder/
        //      .hidden-folder/
        //          42.png
        //      .hidden.txt
        //      not-hidden.txt
        //  text.txt

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("folder/").append(nl).append(reset);
        expected.append(white).append("   image.png").append(nl).append(reset);
        expected.append(white).append("   nested-folder/").append(nl).append(reset);
        expected.append(white).append("      not-hidden.txt").append(nl).append(reset);
        expected.append(white).append("   text.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTree_ExactOutput_WithCustomPrintStream(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    .hidden.txt
        //    Apple.txt
        //    banana.txt
        //    Documents/
        //       images/
        //          Cat.png
        //          cat.png
        //          Dog.png
        //       notes.txt
        //       README.md
        //    zebra.txt

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "banana.txt");
        File zebra = new File(myFolder, "zebra.txt");
        apple.createNewFile();
        banana.createNewFile();
        zebra.createNewFile();

        // Create a hidden file in myFolder
        File hidden = new File(myFolder, ".hidden.txt");
        hidden.createNewFile();

        // Create subdirectory "Documents" in myFolder
        File documents = new File(myFolder, "Documents");
        assertTrue(documents.mkdir(), "Documents directory should be created");

        // Create files in Documents
        File readme = new File(documents, "README.md");
        File notes = new File(documents, "notes.txt");
        readme.createNewFile();
        notes.createNewFile();

        // Create subdirectory "images" in Documents
        File images = new File(documents, "images");
        assertTrue(images.mkdir(), "images directory should be created");

        // Create files in images
        File cat = new File(images, "cat.png");
        File dog = new File(images, "Dog.png");
        cat.createNewFile();
        dog.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(myFolder, false, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl).append(reset);
        expected.append(purple).append("   Apple.txt").append(nl).append(reset);
        expected.append(purple).append("   banana.txt").append(nl).append(reset);
        expected.append(purple).append("   Documents/").append(nl).append(reset);
        expected.append(yellow).append("      images/").append(nl).append(reset);
        expected.append(white).append("         cat.png").append(nl).append(reset);
        expected.append(white).append("         Dog.png").append(nl).append(reset);
        expected.append(yellow).append("      notes.txt").append(nl).append(reset);
        expected.append(yellow).append("      README.md").append(nl).append(reset);
        expected.append(purple).append("   zebra.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testNoOrderColor(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    Apple.txt
        //    banana.txt
        //    Documents/
        //       images/
        //          Cat.png
        //          cat.png
        //          Dog.png
        //    zebra.txt

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "banana.txt");
        File zebra = new File(myFolder, "zebra.txt");
        apple.createNewFile();
        banana.createNewFile();
        zebra.createNewFile();

        // Create subdirectory "Documents" in myFolder
        File documents = new File(myFolder, "Documents");
        assertTrue(documents.mkdir(), "Documents directory should be created");

        // Create subdirectory "images" in Documents
        File images = new File(documents, "images");
        assertTrue(images.mkdir(), "images directory should be created");

        // Create files in images
        File cat = new File(images, "cat.png");
        File dog = new File(images, "Dog.png");
        cat.createNewFile();
        dog.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = false
        TruffulaOptions options = new TruffulaOptions(myFolder, false, false);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        String white = "\033[0;37m";
        String reset = "\033[0m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl).append(reset);
        expected.append(white).append("   Apple.txt").append(nl).append(reset);
        expected.append(white).append("   banana.txt").append(nl).append(reset);
        expected.append(white).append("   Documents/").append(nl).append(reset);
        expected.append(white).append("      images/").append(nl).append(reset);
        expected.append(white).append("         cat.png").append(nl).append(reset);
        expected.append(white).append("         Dog.png").append(nl).append(reset);
        expected.append(white).append("   zebra.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTreeNoColorWithHidden(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    .hidden.txt

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create a hidden file in myFolder
        File hidden = new File(myFolder, ".hidden.txt");
        hidden.createNewFile();
        // try catch if dos, handle in exception 
        Path path = Paths.get(hidden.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        assertTrue(hidden.isHidden());

        // Set up TruffulaOptions with showHidden = false and useColor = false
        TruffulaOptions options = new TruffulaOptions(myFolder, false, false);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testNoOrderWithColor(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    Apple.txt
        //    banana.txt
        //    Documents/
        //       images/
        //          Cat.png
        //          cat.png
        //          Dog.png
        //    zebra.txt

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "banana.txt");
        File zebra = new File(myFolder, "zebra.txt");
        apple.createNewFile();
        banana.createNewFile();
        zebra.createNewFile();

        // Create subdirectory "Documents" in myFolder
        File documents = new File(myFolder, "Documents");
        assertTrue(documents.mkdir(), "Documents directory should be created");

        // Create subdirectory "images" in Documents
        File images = new File(documents, "images");
        assertTrue(images.mkdir(), "images directory should be created");

        // Create files in images
        File cat = new File(images, "cat.png");
        File dog = new File(images, "Dog.png");
        cat.createNewFile();
        dog.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(myFolder, false, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        StringBuilder expected = new StringBuilder();
       
        expected.append(white).append("myFolder/").append(nl).append(reset);
        expected.append(purple).append("   Apple.txt").append(nl).append(reset);
        expected.append(purple).append("   banana.txt").append(nl).append(reset);
        expected.append(purple).append("   Documents/").append(nl).append(reset);
        expected.append(yellow).append("      images/").append(nl).append(reset);
        expected.append(white).append("         cat.png").append(nl).append(reset);
        expected.append(white).append("         Dog.png").append(nl).append(reset);
        expected.append(purple).append("   zebra.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testNoColorSortedAlphabetically(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // Alphabet/
        //    a.txt
        //    b.txt
        //    c/
        //      d.txt
        //      e.txt
        //    zebra.txt

        // Create "Alphabet"
        File alphabet = new File(tempDir, "Alphabet");
        assertTrue(alphabet.mkdir(), "Alphabet should be created");

        // Create files in Alphabet
        File zebra = new File(alphabet, "zebra.txt");
        File b = new File(alphabet, "b.txt");
        File a = new File(alphabet, "a.txt");
        zebra.createNewFile();
        b.createNewFile();
        a.createNewFile();
        
        // Create subdirectory "c" in myFolder
        File c = new File(alphabet, "c");
        assertTrue(c.mkdir(), "Documents directory should be created");

        // Create files in myFolder
        File e = new File(c, "e.txt");
        File d = new File(c, "d.txt");
        e.createNewFile();
        d.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = false
        TruffulaOptions options = new TruffulaOptions(alphabet, false, false);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        String reset = "\033[0m";
        String white = "\033[0;37m";

        StringBuilder expected = new StringBuilder();

        expected.append(white).append("Alphabet/").append(nl).append(reset);
        expected.append(white).append("   a.txt").append(nl).append(reset);
        expected.append(white).append("   b.txt").append(nl).append(reset);
        expected.append(white).append("   c/").append(nl).append(reset);
        expected.append(white).append("      d.txt").append(nl).append(reset);
        expected.append(white).append("      e.txt").append(nl).append(reset);
        expected.append(white).append("   zebra.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testRootDirectoryNull(@TempDir File tempDir) throws IOException {
        File folder = new File(tempDir, "folder");
        folder.mkdir();
        File image = new File(folder, "image.png");
        File text = new File(folder, "text.txt");
        image.createNewFile();
        text.createNewFile();

        // Set up TruffulaOptions with null root directory
        TruffulaOptions options = new TruffulaOptions(null, true, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree and expect exception (since root is null)
        assertThrows(IllegalArgumentException.class, () -> printer.printTree());

        // Verify that no output was printed
        assertTrue(baos.toString().isEmpty(), "Output should be empty when root directory is null");
    }
}
