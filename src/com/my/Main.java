package com.my;

import java.io.IOException;
import java.lang.String;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.objectweb.asm.Opcodes;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class Main {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws ParserConfigurationException {
        // wczytanie pliku Jar
        if (args.length != 2) {
            System.out.println("Please provide a JAR filename and XML output path");
            System.exit(-1);
        }

        Path jarPath = Paths.get(args[0]);
        Path output = Paths.get(args[1]);

        // utworzenie visitora i konwertera
        MyClassVisitor myClassVisitor = new MyClassVisitor(Opcodes.ASM4);
        JarToXmlConverter jarToXmlConverter = null;
        try {
            jarToXmlConverter = new JarToXmlConverter(jarPath,output, myClassVisitor);
            // wizytowanie klas i generowanie dokumentu XML
            jarToXmlConverter.convert();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (TransformerException e) {
            e.printStackTrace();
        }


    }

}
