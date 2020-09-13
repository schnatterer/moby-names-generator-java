package {{packageName}};

import java.util.Random;

public final class MobyNamesGenerator {

    private static Random RANDOM = new Random();

    private static String[] left = new String[]{
        {{{left}}}
    };

    private static String[] right = new String[]{
        {{{right}}}
    };

    /**
    *  GetRandomName generates a random name from the list of adjectives and surnames in this package
    *  formatted as "adjective_surname". For example 'focused_turing'. If retry is non-zero, a random
    *  integer between 0 and 10 will be added to the end of the name, e.g `focused_turing3`
    */
    public static String getRandomName(int retry) {
        String name = left[RANDOM.nextInt(left.length)] + "_" + right[RANDOM.nextInt(right.length)];

        if (name.equals("boring_wozniak")) { // Steve Wozniak is not boring
            return getRandomName(retry);
        }

        if (retry > 0) {
            name += RANDOM.nextInt(11);
        }

        return name;
    }

    public static String getRandomName() {
        return getRandomName(0);
    }
}