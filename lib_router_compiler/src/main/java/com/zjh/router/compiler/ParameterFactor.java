package com.zjh.router.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.zjh.router.annotation.Parameter;

import java.lang.reflect.Type;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class ParameterFactor {

    private MethodSpec.Builder method;
    private ClassName className;
    private Messager messager;
    private Types typeUtils;
    private TypeMirror callMirror;

    public ParameterFactor(Builder builder) {
        this.messager = builder.messager;
        this.className = builder.className;
        this.typeUtils = builder.typeUtils;

        method = MethodSpec.methodBuilder(ProcessorConfig.PARAMETER_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(builder.parameterSpec);
        callMirror = builder.elementUtils.getTypeElement(ProcessorConfig.CALL).asType();
    }

    public void addFirstStatement(){
        method.addStatement("$T t = ($T)" + ProcessorConfig.PARAMETER_NAME, className, className);
    }

    public void buildStatement(Element element){
        TypeMirror typeMirror = element.asType();
        int type = typeMirror.getKind().ordinal();
        String fieldName = element.getSimpleName().toString();
        String annotationValue = element.getAnnotation(Parameter.class).name();
        annotationValue = ProcessorUtil.isEmpty(annotationValue) ? fieldName : annotationValue;


        String finalValue = "t." + fieldName;

        String methodContent = finalValue + " = t.getIntent().";

        if (type == TypeKind.INT.ordinal()){
            methodContent += "getIntExtra($S, "+ finalValue + ")";
        }else if (type == TypeKind.BOOLEAN.ordinal()){
            methodContent += "getBooleanExtra($S, " + fieldName + ")";
        }else {
            if (typeMirror.toString().equalsIgnoreCase(ProcessorConfig.STRING)){
                methodContent += "getStringExtra($S)";
            }else if (typeUtils.isSubtype(typeMirror, callMirror)){
                methodContent = "t." + fieldName + " = ($S) $T.getInstance().build($S).navigation(t)";
                method.addStatement(methodContent,
                        TypeName.get(typeMirror),
                        ClassName.get(ProcessorConfig.ROUTER_API_PACKAGE, ProcessorConfig.ROUTER_MANAGER),
                        annotationValue);
                return;
            }else {
                methodContent = "t.getIntent().getSerializableExtra($S)";
            }
        }

        if (methodContent.contains("Serializable")){
            method.addStatement(finalValue + "=($T)" + methodContent, ClassName.get(element.asType()), annotationValue);
        } else if (methodContent.endsWith(")")){
            method.addStatement(methodContent, annotationValue);
        }else {
            messager.printMessage(Diagnostic.Kind.ERROR, "目前暂支持String、int、boolean传参");
        }

    }

    public MethodSpec build(){
        return method.build();
    }


    public static class Builder{

        private Elements elementUtils;
        private ClassName className;
        private Messager messager;
        private Types typeUtils;
        private ParameterSpec parameterSpec;

        public Builder(ParameterSpec parameterSpec) {
            this.parameterSpec = parameterSpec;
        }

        public Builder setMessager(Messager messager){
            this.messager = messager;
            return this;
        }

        public Builder setElementUtils(Elements elementUtils){
            this.elementUtils = elementUtils;
            return this;
        }

        public Builder setTypeUtils(Types typeUtils){
            this.typeUtils = typeUtils;
            return this;
        }

        public Builder setClassName(ClassName className){
            this.className = className;
            return this;
        }

        public ParameterFactor build(){
            if (parameterSpec == null){
                throw new IllegalArgumentException("parameterSpec方法参数为空");
            }
            if (className == null){
                throw new IllegalArgumentException("方法内容中className为空");
            }
            if (messager == null){
                throw new IllegalArgumentException("messager为空");
            }

            return new ParameterFactor(this);
        }

    }
}
