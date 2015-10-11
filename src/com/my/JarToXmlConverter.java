package com.my;

import org.objectweb.asm.ClassReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by marcin on 11.10.15.
 */
public class JarToXmlConverter {

    private Document doc;
    File outputXml;
    List<JarEntry> listOfClasses;
    JarFile jar;
    private MyClassVisitor myClassVisitor;
    private ClassReader classReader;

    public JarToXmlConverter(Path jarFilePath, Path outputXmlPath, MyClassVisitor myClassVisitor) throws ParserConfigurationException, IOException {
        if (Files.exists(jarFilePath) == false) throw new FileNotFoundException();
        this.outputXml = outputXmlPath.toFile();
        this.jar = new JarFile(jarFilePath.toFile());
        this.myClassVisitor = myClassVisitor;

        // sprawdzenie czy myclassVisitor  i outputXML != null
        if (myClassVisitor == null || outputXmlPath == null) throw new NullPointerException();

        listOfClasses = new ArrayList<JarEntry>();

        // init Document, set DOM structure
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.newDocument();
        myClassVisitor.setDoc(doc);

        // root element - pakiet
        Element rootElement = doc.createElement("jar");
        rootElement.setAttribute("name", jarFilePath.toString());
        doc.appendChild(rootElement);
        myClassVisitor.setRootElement(rootElement);

        // load list of classes from jar file
        loadListOfClasses(jar);
    }

    // metoda konwertujaca
    public void convert() throws TransformerException, IOException {


        // Dla wszystkich klas
        for (JarEntry entry : listOfClasses) {
            InputStream jarIs = jar.getInputStream(entry);


            // Tworz class reader i odwiedzaj
            classReader = new ClassReader(jarIs);
            classReader.accept(myClassVisitor, 0);
        }
        // writeDocToXml
        writeDocToXml();
    }


    private void loadListOfClasses(JarFile root) throws IOException {
        Enumeration<JarEntry> jarEntryEnumeration = root.entries();
        while(jarEntryEnumeration.hasMoreElements()) {
            JarEntry e = jarEntryEnumeration.nextElement();
            if(!e.isDirectory() && e.getName().endsWith(".class")) {
                listOfClasses.add(e);

            }
        }
    }

    private void writeDocToXml() throws TransformerException {
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(outputXml);

        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);

        System.out.println("File saved!");
    }


}
