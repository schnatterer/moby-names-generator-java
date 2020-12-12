package info.schnatterer.mobynamesgenerator.processor;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.google.common.base.Joiner;
import com.google.common.truth.Truth;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubject;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import org.junit.Test;

import javax.tools.JavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;

public class NamesGeneratorProcessorTest {

    private static final String EXPECTED_LEFT = "\"admiring\"";
    private static final String EXPECTED_RIGHT = "\"albattani\"";
    private static final String EXPECTED_PACKAGE_NAME = "com.example";

    private String expectedErrorMissingCompilerArg = "Compile Arg \"mobyVersion\" not set.";
    private String expectedVersion = "1.2.3";
    
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

    private NamesGeneratorProcessor processor = new NamesGeneratorProcessor(url -> {
        assertThat(url).isEqualTo(String.format(NamesGeneratorProcessor.GO_SRC_FILE_URL_TEMPLATE, expectedVersion));
        return new ByteArrayInputStream(goSrc.getBytes(StandardCharsets.UTF_8));
    });

    private final JavaFileObject packageInput = JavaFileObjects.forSourceString(
            EXPECTED_PACKAGE_NAME + ".package-info",
        Joiner.on(System.lineSeparator()).join(
            "@NamesGenerator",
            "package " + EXPECTED_PACKAGE_NAME  + ";",
            "import info.schnatterer.mobynamesgenerator.processor.NamesGenerator;"
        )
    );

    @Test
    public void parsesGoArray() throws IOException {
        NamesGeneratorProcessor processor = new NamesGeneratorProcessor();
        assertThat(processor.parseGoArray("left", goSrc)).isEqualTo(EXPECTED_LEFT);
        assertThat(processor.parseGoArray("right", goSrc)).isEqualTo(EXPECTED_RIGHT);
    }

    @Test
    public void generates() throws IOException {
        process("-AmobyVersion=" + expectedVersion, packageInput)
            .compilesWithoutError()
            .and()
            .generatesSources(expectedOutput(EXPECTED_PACKAGE_NAME));
    }
    
    @Test
    public void failsParsingGoSrcWhenDuplicateArrays() throws IOException {
        goSrc = goSrc.replaceAll("right =", "left =");
        
        process("-AmobyVersion=" + expectedVersion, packageInput)
                .failsToCompile()
                .withErrorContaining("Found multiple go arrays \"left\" in source file.");
    }
    
    @Test
    public void failsParsingGoSrcWhenNoArrays() throws IOException {
        goSrc = goSrc.replaceAll("right =", "somethingCompletelyDifferent =");
        
        process("-AmobyVersion=" + expectedVersion, packageInput)
                .failsToCompile()
                .withErrorContaining("Failed to find go array \"right\" in source file.");
    }
    
    @Test
    public void compilerArgNotSet() throws IOException {

        process(null, packageInput)
            .failsToCompile()
            .withErrorContaining(expectedErrorMissingCompilerArg);
    }

    @Test
    public void compilerArgEmpty() throws IOException {
        process("-AmobyVersion=", packageInput)
            .failsToCompile()
            .withErrorContaining(expectedErrorMissingCompilerArg);
    }

    private CompileTester process(String compilerArgString, JavaFileObject... sources) throws IOException {

        JavaSourcesSubject src = Truth.assert_()
            .about(JavaSourcesSubjectFactory.javaSources())
            .that(Arrays.asList(sources));

        src = src.withClasspath(Arrays.asList(new File("target/classes")));
        if (compilerArgString != null && !compilerArgString.isEmpty()) {
            src = src.withCompilerOptions(compilerArgString);
        }

        return src.processedWith(processor);
    }

    private JavaFileObject expectedOutput(String packageName) throws IOException {
        NameGeneratorModel model = new NameGeneratorModel(packageName, EXPECTED_LEFT, EXPECTED_RIGHT);

        StringWriter writer = new StringWriter();
        Mustache mustache = new DefaultMustacheFactory().compile(NamesGeneratorProcessor.JAVA_CLASS_TEMPLATE);
        mustache.execute(writer, model).flush();

        return JavaFileObjects.forSourceString(
                "MobyNamesGenerator",
                writer.toString()
        );
    }
}
