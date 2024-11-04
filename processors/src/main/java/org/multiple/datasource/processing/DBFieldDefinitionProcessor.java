package org.multiple.datasource.processing;


import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"org.multiple.datasource.processing.DataSourceProperty"})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class DBFieldDefinitionProcessor extends AbstractProcessor {
    private static final Logger logger = Logger.getLogger(DBFieldDefinitionProcessor.class.getName());

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements
                    = roundEnv.getElementsAnnotatedWith(annotation);

            logger.info(annotatedElements.toString());

            if (!annotatedElements.isEmpty()) {
                Element element = annotatedElements.stream().findFirst().get();
                String className = ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString();


                var staticFields = annotatedElements.stream().peek(el -> {
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "element = " + el + ", class name = " + className);
                        })

                        .collect(Collectors.toMap(
                                el -> el.getAnnotation(DataSourceProperty.class).value(),
                                el -> String.class.getTypeName()));
                try {
                    writeUserDaoClass(className, staticFields);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

    private void writeUserDaoClass(
            String className, Map<String, String> staticFields)
            throws IOException {
        logger.info(className);
        String packageName = null;
        var dotBeforeName = className.lastIndexOf('.');
        if (dotBeforeName > 0) {
            packageName = className.substring(0, dotBeforeName);
        }

        String daoClassName = className + "DAO";
        String daoSimpleClassName = daoClassName
                .substring(dotBeforeName + 1);

        JavaFileObject builderFile = processingEnv.getFiler()
                .createSourceFile(daoSimpleClassName);

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            if (packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }

            out.println("import " + List.class.getTypeName() + ";");

            out.print("public class ");
            out.print(daoSimpleClassName);
            out.println(" {");
            out.println();

            staticFields.forEach((nameOrValue, type) -> {

                out.print("    public static final ");
                out.print(type);
                out.print(" ");
                out.print(nameOrValue.toUpperCase());
                out.print(" = ");
                out.print("\"" + nameOrValue + "\";\n");
            });

            out.print("    public static final List<String> ALL(){ \n");
            out.print("        return ");
            out.print(List.class.getTypeName());
            out.println(".of(" + String.join(",", staticFields.keySet().stream().map(String::toUpperCase).toList()) + ");");
            out.println("     }");


            out.println("}");
        }
    }

}
