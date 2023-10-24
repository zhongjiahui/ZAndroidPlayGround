package com.zjh.router.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.zjh.router.annotation.Router;
import com.zjh.router.annotation.bean.RouterBean;

import java.io.IOException;
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
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;


@AutoService(Processor.class)
@SupportedAnnotationTypes({ProcessorConfig.ROUTER_PACKAGE})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions({ProcessorConfig.OPTIONS, ProcessorConfig.APT_PACKAGE})
public class RouterProcessor extends AbstractProcessor {

    // 操作Element的工具类，类、函数、属性
    private Elements elementTool;
    // Type 的工具类，包含用于操作TypeMirror的工具方法
    private Types typeTool;
    // 打印日志
    private Messager messager;
    // 文件生成器
    private Filer filer;
    // 各个模块传递过来的模块名
    private String options;
    // 各个模块传递过来的目录，用于统一存放 apt 生成的文件
    private String aptPackage;
    // path 缓存
    private Map<String, List<RouterBean>> mAllPathMap = new HashMap<>();
    // group 缓存
    private Map<String, String> mAllGroupMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        elementTool = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        typeTool = processingEnvironment.getTypeUtils();

        options = processingEnvironment.getOptions().get(ProcessorConfig.OPTIONS);
        aptPackage = processingEnvironment.getOptions().get(ProcessorConfig.APT_PACKAGE);
        messager.printMessage(Diagnostic.Kind.NOTE, "options = " + options);
        messager.printMessage(Diagnostic.Kind.NOTE, "aptPackage = " + aptPackage);
        if (options != null && aptPackage != null){
            messager.printMessage(Diagnostic.Kind.NOTE, "APT 环境搭建完成");
        } else {
            messager.printMessage(Diagnostic.Kind.NOTE, "APT 环境搭建异常");
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.isEmpty()){
            messager.printMessage(Diagnostic.Kind.NOTE, "没有注解");
            return false;
        }

        TypeElement callType = elementTool.getTypeElement(ProcessorConfig.CALL);
        TypeMirror callMirror = callType.asType();

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Router.class);
        TypeElement activityType = elementTool.getTypeElement(ProcessorConfig.ACTIVITY);
        TypeMirror activityMirror = activityType.asType();

        for (Element element : elements){
            String className = element.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE, "被@Router注解的类有：" + className);

            Router router = element.getAnnotation(Router.class);

            RouterBean routerBean = new RouterBean.Builder()
                    .addGroup(router.group())
                    .addPath(router.path())
                    .addElement(element)
                    .build();

            TypeMirror elementMirror = element.asType();
            if (typeTool.isSubtype(elementMirror,activityMirror)){
                routerBean.setTypeEnum(RouterBean.TypeEnum.ACTIVITY);
            }else if (typeTool.isSameType(elementMirror, callMirror)){
                routerBean.setTypeEnum(RouterBean.TypeEnum.CALL);
            }else{
                throw new RuntimeException("@Router注解目前仅用于activity类上");
            }

            if (checkRouterPath(routerBean)){
                messager.printMessage(Diagnostic.Kind.NOTE, "RouterBean check Success : " + routerBean.toString());
                List<RouterBean> routerBeans = mAllPathMap.get(routerBean.getPath());
                if (ProcessorUtil.isEmpty(routerBeans)){
                    routerBeans = new ArrayList<>();
                    routerBeans.add(routerBean);
                    mAllPathMap.put(routerBean.getGroup(), routerBeans);
                }else {
                    routerBeans.add(routerBean);
                }
            }else {
                messager.printMessage(Diagnostic.Kind.ERROR, "@Router注解未按规范配置");
            }
        }

        TypeElement pathType = elementTool.getTypeElement(ProcessorConfig.ROUTER_API_PATH);
        TypeElement groupType = elementTool.getTypeElement(ProcessorConfig.ROUTER_API_GROUP);

        try {
            createPathFile(pathType);
        }catch (IOException e){
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.NOTE, "生成path模板异常： " + e.getMessage());
        }


        try {
            createGroupFile(groupType, pathType);
        }catch (IOException e){
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.NOTE, "生成group模板异常： " + e.getMessage());
        }

        return true;
    }

    private void createGroupFile(TypeElement groupType, TypeElement pathType) throws IOException {
        if (ProcessorUtil.isEmpty(mAllGroupMap) || ProcessorUtil.isEmpty(mAllPathMap)){
            return;
        }
        TypeName methodReturns = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(
                        ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathType)))
        );

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(ProcessorConfig.GROUP_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(methodReturns);

        methodBuilder.addStatement("$T<$T, $T> $N = new $T<>()",
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathType))),
                ProcessorConfig.GROUP_VAR1,
                ClassName.get(HashMap.class));

        for (Map.Entry<String, String> entry : mAllGroupMap.entrySet()){
            methodBuilder.addStatement("$N.put($S, $T.class)",
                    ProcessorConfig.GROUP_VAR1,
                    entry.getKey(),
                    ClassName.get(aptPackage, entry.getValue()));
        }

        methodBuilder.addStatement("return $N", ProcessorConfig.GROUP_VAR1);

        String finalClassName = ProcessorConfig.GROUP_FILE_NAME + options;
        messager.printMessage(Diagnostic.Kind.NOTE, "APT生成路由组Group类文件："+aptPackage + "." + finalClassName);

        JavaFile.builder(aptPackage,
                TypeSpec.classBuilder(finalClassName)
                        .addSuperinterface(ClassName.get(groupType))
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(methodBuilder.build())
                        .build()).
                build()
                .writeTo(filer);
    }



    private void createPathFile(TypeElement pathType) throws IOException {
        if (ProcessorUtil.isEmpty(mAllPathMap)){
            return;
        }

        TypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterBean.class));

        for (Map.Entry<String, List<RouterBean>> entry : mAllPathMap.entrySet()) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(ProcessorConfig.PATH_METHOD_NAME)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(methodReturn);

            methodBuilder.addStatement("$T<$T, $T> $N = new $T<>()",
                    ClassName.get(Map.class),
                    ClassName.get(String.class),
                    ClassName.get(RouterBean.class),
                    ProcessorConfig.PATH_VAR1,
                    ClassName.get(HashMap.class));

            List<RouterBean> pathList = entry.getValue();

            for (RouterBean bean : pathList) {
                methodBuilder.addStatement("$N.put($S, $T.create($T.$L, $T.class, $S, $S))",
                        ProcessorConfig.PATH_VAR1,
                        bean.getPath(),
                        ClassName.get(RouterBean.class),
                        ClassName.get(RouterBean.TypeEnum.class),
                        bean.getTypeEnum(),
                        ClassName.get((TypeElement) bean.getElement()),
                        bean.getPath(),
                        bean.getGroup());
            }

            methodBuilder.addStatement("return $N", ProcessorConfig.PATH_VAR1);

            String finalClassName = ProcessorConfig.PATH_FILE_NAME + entry.getKey();
            messager.printMessage(Diagnostic.Kind.NOTE, "APT生成路由Path类文件：" + aptPackage + "." + finalClassName);

            JavaFile.builder(aptPackage,
                            TypeSpec.classBuilder(finalClassName)
                                    .addSuperinterface(ClassName.get(pathType))
                                    .addModifiers(Modifier.PUBLIC)
                                    .addMethod(methodBuilder.build())
                                    .build())
                    .build()
                    .writeTo(filer);

            mAllGroupMap.put(entry.getKey(), finalClassName);
        }
    }

    private boolean checkRouterPath(RouterBean routerBean) {
        String group = routerBean.getGroup();
        String path = routerBean.getPath();

        if (ProcessorUtil.isEmpty(path) || !path.startsWith("/")){
            messager.printMessage(Diagnostic.Kind.NOTE, "@Router注解中的path值，必须要 / 开头");
            return false;
        }

        if (path.lastIndexOf("/") == 0){
            messager.printMessage(Diagnostic.Kind.NOTE, "@Router注解不规范");
            return false;
        }

        String findGroup = path.substring(1, path.indexOf("/", 1));
        if (!ProcessorUtil.isEmpty(group) && !group.equals(options)){
            messager.printMessage(Diagnostic.Kind.ERROR, "@Router注解中的group值必须和自摸名一致");
            return false;
        }else {
            routerBean.setGroup(findGroup);
        }

        return true;
    }

}
