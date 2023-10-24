package com.zjh.router.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.zjh.router.annotation.Parameter;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes(ProcessorConfig.PARAMETER_PACKAGE)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ParameterProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Types typeUtils;
    private Messager messager;
    private Filer filer;
    // 各个模块传递过来的模块名
    private String options;
    // 各个模块传递过来的目录，用于统一存放 apt 生成的文件
    private String aptPackage;
    private Map<TypeElement, List<Element>> tempParameterMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();

        options = processingEnvironment.getOptions().get(ProcessorConfig.OPTIONS);
        aptPackage = processingEnvironment.getOptions().get(ProcessorConfig.APT_PACKAGE);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!ProcessorUtil.isEmpty(set)){
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Parameter.class);
            if (!ProcessorUtil.isEmpty(elements)){
                for (Element element : elements){
                    TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                    if (tempParameterMap.containsKey(enclosingElement)){
                        tempParameterMap.get(enclosingElement).add(element);
                    } else {
                        ArrayList<Element> fields = new ArrayList<>();
                        fields.add(element);
                        tempParameterMap.put(enclosingElement, fields);
                    }
                }

                if (ProcessorUtil.isEmpty(tempParameterMap)){
                    return true;
                }

                TypeElement activityType = elementUtils.getTypeElement(ProcessorConfig.ACTIVITY);
                TypeElement parameterType = elementUtils.getTypeElement(ProcessorConfig.ROUTER_API_PARAMETER);

                ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.OBJECT, ProcessorConfig.PARAMETER_NAME).build();

                for (Map.Entry<TypeElement, List<Element>> entry : tempParameterMap.entrySet()){
                    TypeElement typeElement = entry.getKey();

                    if (!typeUtils.isSubtype(typeElement.asType(), activityType.asType())) {
                        throw new RuntimeException("@Parameter 注解目前只能使用在Activity上");
                    }

                    ClassName className = ClassName.get(typeElement);

                    ParameterFactor factory = new ParameterFactor.Builder(parameterSpec)
                            .setMessager(messager)
                            .setElementUtils(elementUtils)
                            .setClassName(className)
                            .setTypeUtils(typeUtils)
                            .build();

                    factory.addFirstStatement();

                    for (Element element : entry.getValue()){
                        factory.buildStatement(element);
                    }

                    String finalClassName = typeElement.getSimpleName() + ProcessorConfig.PARAMETER_FILE_NAME;
                    messager.printMessage(Diagnostic.Kind.NOTE, "APT生成获取参数类文件："+ aptPackage + "." + finalClassName);


                    try {
                        JavaFile.builder(aptPackage,
                                TypeSpec.classBuilder(finalClassName)
                                        .addSuperinterface(ClassName.get(parameterType))
                                        .addModifiers(Modifier.PUBLIC)
                                        .addMethod(factory.build())
                                        .build())
                                .build()
                                .writeTo(filer);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        return false;
    }
}
