package info.schnatterer.modules;

import info.schnatterer.mobynamesgenerator.MobyNamesGenerator;

public class GenerateRandomName {

    public static void main(String[] args) {
        //for (int i = 0; i < 1000000; i++) {
            System.out.println(MobyNamesGenerator.getRandomName());
        //}
    }
}
