package info.schnatterer.mobynamesgenerator;

/**
 *  Without this the build will fail with "package is empty or does not exist".
 *  Even though there is the generated class MobyNamesGenerator.
 *
 *  So we create this dummy interface. See:
 *  http://jigsaw-dev.1059479.n5.nabble.com/Exporting-a-package-with-no-Java-sources-td5717155.html
 */
@SuppressWarnings("unused")
interface DontFailWithEmptyPackage {
}
