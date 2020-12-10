package info.schnatterer.mobynamesgenerator.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.PACKAGE})
public @interface NamesGenerator {
}
