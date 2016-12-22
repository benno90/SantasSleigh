package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author benno
 */
public class Instance {

    private final Set<Gift> gifts;

    /**
     * Load an instance from a given csv file.
     *
     * @param filePath The path to the file.
     * @return The loaded instance.
     * @throws IOException If an exception occurs while reading the file.
     */
    public static Instance load(Path filePath) throws IOException {
        try (BufferedReader tspFileReader = Files.newBufferedReader(filePath, Charset.forName("UTF-8"))) {
            List<String> lines = Files.readAllLines(filePath);
            List<String> header = lines.subList(0, 1);
            Set<Gift> gifts = lines.subList(1, lines.size()).stream().map(line -> line.split(","))
                    .map(splits -> new Gift(Integer.parseInt(splits[0]), Double.parseDouble(splits[2]), Double.parseDouble(splits[1]), Double.parseDouble(splits[3])))
                    .collect(Collectors.toSet());
            return new Instance(gifts);
        }
    }

    /**
     * Create a new instance.
     *
     * @param gifts The Gifts.
     */
    public Instance(Set<Gift> gifts) {
        super();
        this.gifts = gifts;
    }

    public Set<Gift> getGifts() {
        return gifts;
    }

}
