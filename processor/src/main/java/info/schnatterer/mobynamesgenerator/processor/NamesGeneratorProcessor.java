package info.schnatterer.mobynamesgenerator.processor;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SupportedSourceVersion(SourceVersion.RELEASE_8)
// compilerArgs: "-AmobyVersion=v19.03.6" -> Docker Tag of https://github.com/moby/moby/blob/v19.03.6/pkg/namesgenerator/names-generator.go
@SupportedOptions({"mobyVersion"})
@SupportedAnnotationTypes("info.schnatterer.mobynamesgenerator.processor.NamesGenerator")
public class NamesGeneratorProcessor extends AbstractProcessor {

    private static final boolean CLAIM_ANNOTATIONS = true;

    static final String JAVA_CLASS_TEMPLATE = "info/schnatterer/mobynamesgenerator/processor/NamesGenerator-template.java";
    static final String GO_SRC_FILE_URL_TEMPLATE = "https://raw.githubusercontent.com/moby/moby/%s/pkg/namesgenerator/names-generator.go";
    private final Downloader downloader;

    private MustacheFactory mustacheFactory = new DefaultMustacheFactory();

    public NamesGeneratorProcessor() {
        this(new DefaultDownloader());
    }

    NamesGeneratorProcessor(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            processAnnotatedElements(roundEnv.getElementsAnnotatedWith(annotation));
        }

        return CLAIM_ANNOTATIONS;
    }

    private void processAnnotatedElements(Set<? extends Element> annotatedElements) {

        for (Element annotatedElement : annotatedElements) {
            processAnnotationInstances(annotatedElement.getAnnotationsByType(NamesGenerator.class), annotatedElement);
        }
    }

    private void processAnnotationInstances(NamesGenerator[] annotationInstances, Element annotatedElement) {

        for (NamesGenerator annotationInstance : annotationInstances) {
            processAnnotationInstance(annotationInstance, annotatedElement);
        }
    }

    private void processAnnotationInstance(NamesGenerator annotationInstance, Element annotatedElement) {

        try {
            String mobyVersion = readAndValidateMobyVersionFromCompilerArg();

            writeClass(mobyVersion, annotatedElement);

        } catch (IOException e) {
            error(e);
        }
    }

    private String readAndValidateMobyVersionFromCompilerArg() throws IOException {
        String mobyVersion = processingEnv.getOptions().get("mobyVersion");
        if (mobyVersion == null || mobyVersion.isEmpty()) {
            throw new IOException("Compile Arg \"mobyVersion\" not set.");
        }
        return mobyVersion;
    }

    private void writeClass(String mobyVersion, Element element) throws IOException {
        Filer filer = processingEnv.getFiler();
        String packageName = findPackageName(element);

        String namesGeneratorSrcFile = downloadNamesGeneratorSourceFile(mobyVersion);
        String left = parseGoArray("left", namesGeneratorSrcFile); 
        String right = parseGoArray("right", namesGeneratorSrcFile); 

        NameGeneratorModel model = new NameGeneratorModel(packageName, left, right);

        JavaFileObject jfo = filer.createSourceFile("MobyNamesGenerator");
        Mustache mustache = mustacheFactory.compile(JAVA_CLASS_TEMPLATE);

        try (Writer writer = jfo.openWriter()) {
            mustache.execute(writer, model).flush();
        }
    }

    String parseGoArray(String arrayName, String namesGeneratorSrcFile) throws IOException {
        Pattern pattern = Pattern.compile("(?s)" + arrayName + " = \\[...\\]string\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(namesGeneratorSrcFile);
        if (matcher.find()) {
            String group = matcher.group(1).trim();
            if (matcher.find()) {
                throw new IOException("Found multiple go arrays \"" + arrayName + "\" in source file.");
            }
            return group;
        } else {
           throw new IOException("Failed to find go array \"" + arrayName + "\" in source file.");
        }
    }

    private String downloadNamesGeneratorSourceFile(String mobyVersion) throws IOException {
        String url = String.format(GO_SRC_FILE_URL_TEMPLATE, mobyVersion);
        try (Scanner scanner = createScanner(url)) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    protected Scanner createScanner(String url) throws IOException {
        return new Scanner(downloader.openUrl(url), StandardCharsets.UTF_8.toString());
    }

    private String findPackageName(Element element) {
        return findPackageNameForPackage(element);
    }

    private String findPackageNameForPackage(Element element) {
        return element.toString();
    }

    private void error(IOException e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "failed to write extension file: " + e.getMessage());
    }

    /**
     * Google compile testing seems to be able to use only the processor itself.
     * Any derived test class or mockito spy causes the Processor to not be triggered during compilation.
     * 
     * So inject this "hook" into the processor for testing :-/
     */
    interface Downloader {
        InputStream openUrl(String url) throws IOException;
    }

    private static class DefaultDownloader implements Downloader {
        @Override
        public InputStream openUrl(String url) throws IOException {
            return new URL(url).openStream();
        }
    }
}