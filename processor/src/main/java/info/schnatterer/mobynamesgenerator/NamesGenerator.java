package info.schnatterer.mobynamesgenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.PACKAGE})
public @interface NamesGenerator {
}
