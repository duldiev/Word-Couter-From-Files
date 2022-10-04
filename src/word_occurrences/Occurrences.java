
package word_occurrences;

import java.io.FileNotFoundException;
import java.util.*;

public class Occurrences {

    // Maps words -> filename -> sets of their Positions in the file.
    private final TreeMap<String, TreeMap<String, TreeSet<FilePosition>>> occMap;

    public Occurrences(String rootDirPath) throws FileNotFoundException {
        occMap = new TreeMap<>();
        FileWalker walker = new FileWalker(rootDirPath, this);
        walker.populateOccurrenceMap();
    }

    /*
        Called by FileWalker to add an occurrence to the occMap
     */
    void put(String word, String filePath, FilePosition pos) {
        word = word.toLowerCase();
        if (occMap.containsKey(word)) {
            if (occMap.get(word).containsKey(filePath)) {
                occMap.get(word).get(filePath).add(pos);
            } else {
                TreeSet<FilePosition> positions = new TreeSet<>();
                positions.add(pos);
                occMap.get(word).put(filePath, positions);
            }
        } else {
            TreeSet<FilePosition> positions = new TreeSet<>();
            positions.add(pos);
            TreeMap<String, TreeSet<FilePosition>> filePathMap = new TreeMap<>();
            filePathMap.put(filePath, positions);
            occMap.put(word, filePathMap);
        }
    }


    /**
     * @return the number of distinct words found in the files
     */
    public int distinctWords() {
        return occMap.size();
    }

    /**
     * @return the number of total word occurrences across all files
     */
    public int totalOccurrences() {
        int totalOccurrences = 0;

        Set<Map.Entry<String, TreeMap<String, TreeSet<FilePosition>>>> entries = occMap.entrySet();
        Iterator<Map.Entry<String, TreeMap<String, TreeSet<FilePosition>>>> iterator
                = entries.iterator();
        Map.Entry<String, TreeMap<String, TreeSet<FilePosition>>> entry = null;

        while (iterator.hasNext()) {
            entry = iterator.next();

            Set<Map.Entry<String, TreeSet<FilePosition>>> entriesPos = entry.getValue().entrySet();
            Iterator<Map.Entry<String, TreeSet<FilePosition>>> iteratorPos
                    = entriesPos.iterator();
            Map.Entry<String, TreeSet<FilePosition>> entryPos = null;

            while (iteratorPos.hasNext()) {
                entryPos = iteratorPos.next();
                totalOccurrences += entryPos.getValue().size();
            }
        }

        return totalOccurrences;
    }

    /**
     * Finds the total number of occurrences of a given word across
     * all files.  If the word is not found among the occurrences,
     * simply return 0.
     *
     * @param word whose occurrences we are counting
     * @return the number of occurrences
     */
    public int totalOccurrencesOfWord(String word) {
        word = word.toLowerCase();

        if (!occMap.containsKey(word) || occMap.get(word) == null)
            return 0;

        int totalOccurrencesOfWord = 0;

        Set<Map.Entry<String, TreeSet<FilePosition>>> entriesPos = occMap.get(word).entrySet();
        Iterator<Map.Entry<String, TreeSet<FilePosition>>> iterator
                = entriesPos.iterator();
        Map.Entry<String, TreeSet<FilePosition>> entry = null;

        while (iterator.hasNext()) {
            entry = iterator.next();
            totalOccurrencesOfWord += entry.getValue().size();
        }
        return totalOccurrencesOfWord;
    }

    /**
     * Finds the total number of occurrences of a given word in the given file.
     * If the file is not found in Occurrences, or if the word does not occur
     * in the file, simply return 0.
     *
     * @param word whose occurrences we are counting
     * @param filepath of the file
     * @return the number of occurrences
     */
    public int totalOccurrencesOfWordInFile(String word, String filepath) {
        word = word.toLowerCase();

        if (!occMap.containsKey(word) || occMap.get(word) == null)
            return 0;

        int totalOccurrencesOfWordInFile = 0;

        Set<Map.Entry<String, TreeSet<FilePosition>>> entries = occMap.get(word).entrySet();
        Iterator<Map.Entry<String, TreeSet<FilePosition>>> iterator = entries.iterator();
        Map.Entry<String, TreeSet<FilePosition>> entry = null;

        while (iterator.hasNext()) {
            entry = iterator.next();

            if (entry.getKey().equals(filepath)) {
                totalOccurrencesOfWordInFile += entry.getValue().size();
            }
        }
        return totalOccurrencesOfWordInFile;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();

        Set<Map.Entry<String, TreeMap<String, TreeSet<FilePosition>>>> entries = occMap.entrySet();
        Iterator<Map.Entry<String, TreeMap<String, TreeSet<FilePosition>>>> iterator
                = entries.iterator();
        Map.Entry<String, TreeMap<String, TreeSet<FilePosition>>> entry = null;

        while (iterator.hasNext()) {
            entry = iterator.next();

            String word = entry.getKey();

            sb.append("\""+ word + "\" has " + totalOccurrencesOfWord(word) +
                    " occurrence(s):\n");

            Set<Map.Entry<String, TreeSet<FilePosition>>> entriesPos = entry.getValue().entrySet();
            Iterator<Map.Entry<String, TreeSet<FilePosition>>> iteratorPos
                    = entriesPos.iterator();
            Map.Entry<String, TreeSet<FilePosition>> entryPos = null;

            while (iteratorPos.hasNext()) {
                entryPos = iteratorPos.next();
                String filepath = entryPos.getKey();

                for (FilePosition position : entryPos.getValue()) {
                    String pos = position.toString();
                    sb.append("   ( file: \"" + filepath + "\"; " + pos + " )\n");
                }
            }
        }

        return sb.toString();
    }
}


