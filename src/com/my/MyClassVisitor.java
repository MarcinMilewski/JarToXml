package com.my;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by marcin on 10.10.15.
 */
public class MyClassVisitor extends ClassVisitor {

    // DOM document
    private Document doc;

    private Element rootElement;    // jar
    private Element lastClassElement;
    private Element lastMethodsElement;
    private Element lastFieldsElement;
    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public void setRootElement(Element rootElement) {
        this.rootElement = rootElement;
    }

    public MyClassVisitor(int i) {
        super(i);


    }

    public MyClassVisitor(int i, ClassVisitor classVisitor) {
        super(i, classVisitor);
    }


    /*
            * Called when a class is visited. This is the method called first
            */
    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
//        System.out.println("Visiting class: " + name);
//        System.out.println("Class Major Version: " + version);
//        System.out.println("Super class: " + superName);

        // dodanie pola klasa
        lastClassElement = doc.createElement("class");
        rootElement.appendChild(lastClassElement);

        // dodanie atrybutu name
        Element className = doc.createElement("name");
        className.appendChild(doc.createTextNode(name));
        lastClassElement.appendChild(className);

        // dodanie atrybutu SuperClass
        superName = (name == null) ? "null" : name;
        Element superClassName = doc.createElement("superClass");
        superClassName.appendChild(doc.createTextNode(superName));
        lastClassElement.appendChild(superClassName);

        // dodanie pola: pola
        lastFieldsElement = doc.createElement("fields");
        lastClassElement.appendChild(lastFieldsElement);

        // dodanie pola: metody
        lastMethodsElement = doc.createElement("methods");
        lastClassElement.appendChild(lastMethodsElement);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    /*
     * Invoked only when the class being visited is an inner class
     */
    @Override
    public void visitOuterClass(String owner, String name, String desc) {
        //System.out.println("Outer class: "+owner);
        super.visitOuterClass(owner, name, desc);
    }

    /*
     *Invoked when a class level annotation is encountered
     */
    @Override
    public AnnotationVisitor visitAnnotation(String desc,
                                             boolean visible) {
       // System.out.println("Annotation: "+desc);
        return super.visitAnnotation(desc, visible);
    }

    /*
     * When a class attribute is encountered
     */
    @Override
    public void visitAttribute(Attribute attr) {
        //System.out.println("Class Attribute: "+attr.type);
        super.visitAttribute(attr);
    }

    /*
     *When an inner class is encountered
     */
    @Override
    public void visitInnerClass(String name, String outerName,
                                String innerName, int access) {
        //System.out.println("Inner Class: "+ innerName+" defined in "+outerName);
        super.visitInnerClass(name, outerName, innerName, access);
    }

    /*
     * When a field is encountered
     */
    @Override
    public FieldVisitor visitField(int access, String name,
                                   String desc, String signature, Object value) {
        System.out.println("Field: "+name+" "+desc+" value:"+value);

        // dodanie pola Pole
        Element field = doc.createElement("field");
        lastFieldsElement.appendChild(field);

        // dodanie pola nazwa
        name = (name == null) ? "null" : name;
        Element fieldName = doc.createElement("name");
        fieldName.appendChild(doc.createTextNode(name));
        field.appendChild(fieldName);

        // dodanie pola access
        Element fieldAccess = doc.createElement("access");
        fieldAccess.appendChild(doc.createTextNode(Integer.toString(access)));
        field.appendChild(fieldAccess);

        // dodanie pola desc
        desc = (desc == null) ? "null" : desc;
        Element fieldDesc = doc.createElement("desc");
        fieldDesc.appendChild(doc.createTextNode(desc));
        field.appendChild(fieldDesc);

        // dodanie pola signature
        String fSignature = (signature == null) ? "null" : signature;
        Element fieldSignature = doc.createElement("signature");
        fieldSignature.appendChild(doc.createTextNode(fSignature));
        field.appendChild(fieldSignature);

        // dodanie pola value

        Element fieldValue = doc.createElement("value");
        String tmp = (value == null) ? "null" : value.toString();
        fieldValue.appendChild(doc.createTextNode(tmp));
        field.appendChild(fieldValue);

        return super.visitField(access, name, desc, signature, value);
    }


    @Override
    public void visitEnd() {
        //System.out.println("Method ends here");
        super.visitEnd();
    }

    /*
     * When a method is encountered
     */
    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
       // System.out.println("Method: "+name+" "+desc);
        // dodanie pola Metoda
        Element method = doc.createElement("method");
        lastMethodsElement.appendChild(method);

        // dodanie pola nazwa
        name = (name == null) ? "null" : name;
        Element methodName = doc.createElement("name");
        methodName.appendChild(doc.createTextNode(name));
        method.appendChild(methodName);

        // dodanie pola access
        Element methodAccess = doc.createElement("access");
        methodAccess.appendChild(doc.createTextNode(Integer.toString(access)));
        method.appendChild(methodAccess);

        // dodanie pola desc
        desc = (desc == null) ? "null" : desc;
        Element methodDesc = doc.createElement("desc");
        methodDesc.appendChild(doc.createTextNode(desc));
        method.appendChild(methodDesc);

        // dodanie pola signature
        signature = (signature == null) ? "null" : signature;
        Element methodSignature = doc.createElement("signature");
        methodSignature.appendChild(doc.createTextNode(signature));
        method.appendChild(methodSignature);

        // dodanie Exceptions
        if(exceptions != null) {
            if (exceptions.length > 0) {
                // dodaj Pole Exceptions
                Element methodExceptions = doc.createElement("exceptions");
                method.appendChild(methodExceptions);

                // dla kazdego dodaj pole Exception
                for (String exception : exceptions) {
                    Element methodException = doc.createElement("exception");
                    methodExceptions.appendChild(methodException);

                    // dodaj pole name
                    Element exceptionName = doc.createElement("name");
                    exceptionName.appendChild(doc.createTextNode(exception));
                    methodException.appendChild(exceptionName);
                }
            }
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    /*
     * When the optional source is encountered
     */
    @Override
    public void visitSource(String source, String debug) {
        //System.out.println("Source: "+source);
        super.visitSource(source, debug);
    }
}
