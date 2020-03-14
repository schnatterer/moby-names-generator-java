package info.schnatterer.mobynamesgenerator;

import com.github.mustachejava.MustacheFactory;
import com.google.common.base.Joiner;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

public class NamesGeneratorProcessorTest {

    private static final String EXPECTED_LEFT = "\"admiring\"";
    private static final String EXPECTED_RIGHT = "\"albattani\"";

    private String goSrc = Joiner.on(System.lineSeparator()).join(
            "package namesgenerator // import \"github.com/docker/docker/pkg/namesgenerator\"\n",
            "",
            "import (",
            "\"fmt\"",
            ")",
            "var (",
            "left = [...]string{",
            EXPECTED_LEFT,
            "}",
            "",
            "right = [...]string{",
            EXPECTED_RIGHT,
            "}",
            "func GetRandomName(retry int) string {",
            "begin:",
            "return \"a\"",
            "}"
    );

    @Test
    public void parsesGoArray() {
        NamesGeneratorProcessor processor = new NamesGeneratorProcessor(mock(MustacheFactory.class));
        assertThat(processor.parseGoArray("left", goSrc)).isEqualTo(EXPECTED_LEFT);
        assertThat(processor.parseGoArray("right", goSrc)).isEqualTo(EXPECTED_RIGHT);
    }
}
