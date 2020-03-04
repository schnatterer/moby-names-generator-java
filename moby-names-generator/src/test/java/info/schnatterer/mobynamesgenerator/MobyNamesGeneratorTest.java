package info.schnatterer.mobynamesgenerator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MobyNamesGeneratorTest {

    @Test
    void getRandomName() {
        assertThat(MobyNamesGenerator.getRandomName(0).contains("_"));
    }
}
