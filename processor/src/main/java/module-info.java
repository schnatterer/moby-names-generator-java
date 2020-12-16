module info.schnatterer.mobynamesgenerator.processor {
    requires java.compiler;
    requires com.github.mustachejava;

    provides javax.annotation.processing.Processor with info.schnatterer.mobynamesgenerator.processor.NamesGeneratorProcessor;
    exports info.schnatterer.mobynamesgenerator.processor;
}