package info.schnatterer.mobynamesgenerator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.kohsuke.MetaInfServices;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;


@SupportedSourceVersion(SourceVersion.RELEASE_8)
// compilerArgs: "-AmobyVersion=v19.03.6" -> Docker Tag of https://github.com/moby/moby/blob/v19.03.6/pkg/namesgenerator/names-generator.go
@SupportedOptions({"mobyVersion"})
@SupportedAnnotationTypes("info.schnatterer.mobynamesgenerator.NamesGenerator")
@MetaInfServices(Processor.class)
public class NamesGeneratorProcessor extends AbstractProcessor {

    private static final boolean CLAIM_ANNOTATIONS = true;

    private static final String TEMPLATE = "info/schnatterer/mobynamesgenerator/NamesGenerator-template.java";

    private MustacheFactory mustacheFactory;

    public NamesGeneratorProcessor() {
        this( new DefaultMustacheFactory());
    }

    NamesGeneratorProcessor(MustacheFactory mustacheFactory) {
        this.mustacheFactory = mustacheFactory;
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
        // TODO download and parse https://raw.githubusercontent.com/moby/moby/v19.03.6/pkg/namesgenerator/names-generator.go
        String left = "\"admiring\"";
        String right = "\"zhukovsky\"";

        NameGeneratorModel model = new NameGeneratorModel(packageName, left, right);

        JavaFileObject jfo = filer.createSourceFile("MobyNamesGenerator");
        Mustache mustache = mustacheFactory.compile(TEMPLATE);

        try (Writer writer = jfo.openWriter()) {
            mustache.execute(writer, model).flush();
        }
    }

    private String findPackageName(Element element) {
        if (element.getEnclosingElement() != null) {
            return findPackageNameForClass(element);
        }
        return findPackageNameForPackage(element);
    }

    private String findPackageNameForPackage(Element element) {
        return element.toString();
    }

    private String findPackageNameForClass(Element element) {
        if (element.getEnclosingElement().getSimpleName().toString().isEmpty()) {
            return findPackageNameForClassInDefaultPackage();
        }
        return element.getEnclosingElement().toString();
    }

    private String findPackageNameForClassInDefaultPackage() {
        return "";
    }

    private void error(IOException e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "failed to write extension file: " + e.getMessage());
    }
}
