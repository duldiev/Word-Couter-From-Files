
package word_occurrences;

import java.io.*;
import java.util.Objects;


final class FileWalker {

    private final Occurrences occ;
    private final File rootDir;

    FileWalker(String rootDirPath, Occurrences occ) throws FileNotFoundException {
        this.occ = occ;
        this.rootDir = new File(rootDirPath);

        if (!this.rootDir.isDirectory()) {
            throw new FileNotFoundException(rootDirPath + " does not exist, " +
                    "or is not a directory.");
        }
    }

    void populateOccurrenceMap() {
        try {
            populateOccurrenceMap(rootDir);
        } catch (IOException ex) {
            // We should never really get to this point, but just in case...
            System.out.println(ex);
        }
    }

    private void populateOccurrenceMap(File fileOrDir) throws IOException {
        if (!fileOrDir.exists()) {
            System.out.println("File or directory does not exist.");
            return;
        }
        if (fileOrDir.isDirectory()) {
            for (File file : Objects.requireNonNull(fileOrDir.listFiles()))
                populateOccurrenceMap(file);
            return;
        }
        if (fileOrDir.isFile()) {
            BufferedReader reader = new BufferedReader(new FileReader((fileOrDir)));
            int ch;
            StringBuilder word = new StringBuilder();

            int line = 1;
            int column = 1;

            do {
                ch = reader.read();
                char chChar = (char)ch;


                if (Syntax.isNewLine(chChar) || !Syntax.isInWord(chChar)) {
                    if (word.length() > 0) {
                        occ.put(word.toString(), fileOrDir.getPath(), new FilePosition(line, column));
                    }
                    word.delete(0, word.length());
                }

                if (Syntax.isNewLine(chChar)) {
                    line++;
                    column = 1;
                }

//                    if (fileOrDir.getPath().equals("test_dir/Music/Johnny Cash/A Boy Named Sue.txt"))
//                        System.out.println(chChar);
                column++;

                if (Syntax.isInWord(chChar))
                    word.append(chChar);
            } while (ch != -1);

            reader.close();
        }
    }
}


