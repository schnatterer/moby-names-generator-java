package info.schnatterer.mobynamesgenerator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MobyNamesGeneratorTest {
    
    private static Set<String> expectedNames;
    private static Set<String> expectedAttributes;

    @BeforeAll
    static void setup() throws Exception {
        expectedAttributes = new HashSet(Arrays.asList((String[]) getPrivateStaticField(MobyNamesGenerator.class, "left")));
        expectedNames = new HashSet(Arrays.asList((String[]) getPrivateStaticField(MobyNamesGenerator.class, "right")));
    }

    @Test
    void getRandomName() {
        Set<String> attribs = new HashSet<>();
        Set<String> names = new HashSet<>();
        Set<String> combos = new HashSet<>();

        for (int i = 0; i < 1000000; i++) {
            String randomName = MobyNamesGenerator.getRandomName(0);
            assertThat(randomName).contains("_");
            
            String[] split = randomName.split("_");
            attribs.add(split[0]);
            names.add(split[1]);
            combos.add(randomName);
            assertThat(randomName).isNotEqualTo("boring_wozniak");
        }
        assertThat(attribs).isEqualTo(expectedAttributes);
        assertThat(names).isEqualTo(expectedNames);
        assertThat(combos.size()).isEqualTo(expectedAttributes.size() * expectedNames.size() - 1);
    }

    public static Object getPrivateStaticField(Class<?> clazz, String field) throws Exception {
        Field f = clazz.getDeclaredField(field);
        f.setAccessible(true);
        return f.get(clazz);
    }
}
