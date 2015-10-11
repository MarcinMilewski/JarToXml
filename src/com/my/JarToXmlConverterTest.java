package com.my;

import org.junit.Test;
import org.objectweb.asm.Opcodes;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by marcin on 11.10.15.
 */
public class JarToXmlConverterTest {

    @Test
    public void testCreation() throws Exception {
        Path jar = Paths.get("/home/marcin/Downloads/asm-all-3.3.jar");
        Path output = Paths.get("junitXml.xml");
        MyClassVisitor myClassVisitor = new MyClassVisitor(Opcodes.ASM4);
        JarToXmlConverter jarToXmlConverter = new JarToXmlConverter
                (jar,output,myClassVisitor);
    }

    @Test (expected = IOException.class)
    public void testShouldCatchExceptionOnCreation() throws IOException, ParserConfigurationException {
        Path jar = Paths.get("/home/marcin/Downloads/a-all-3.3.jar");
        Path output = Paths.get("junitXml.xml");
        MyClassVisitor myClassVisitor = new MyClassVisitor(Opcodes.ASM4);
        JarToXmlConverter jarToXmlConverter = new JarToXmlConverter
                (jar,output,myClassVisitor);
    }

    @Test
    public void testLoadListOfClasses() throws Exception {
        Path jar = Paths.get("/home/marcin/Downloads/asm-all-3.3.jar");
        Path output = Paths.get("junitXml.xml");
        MyClassVisitor myClassVisitor = new MyClassVisitor(Opcodes.ASM4);
        JarToXmlConverter jarToXmlConverter = new JarToXmlConverter
                (jar,output, myClassVisitor);
        assertTrue(jarToXmlConverter.listOfClasses.size() == 159);
    }

    @Test
    public void testConvert() throws Exception {
        Path jar = Paths.get("/home/marcin/Downloads/asm-all-3.3.jar");
        Path output = Paths.get("testXml.xml");
        MyClassVisitor myClassVisitor = new MyClassVisitor(Opcodes.ASM4);
        JarToXmlConverter jarToXmlConverter = new JarToXmlConverter
                (jar,output, myClassVisitor);
        jarToXmlConverter.convert();
    }


}