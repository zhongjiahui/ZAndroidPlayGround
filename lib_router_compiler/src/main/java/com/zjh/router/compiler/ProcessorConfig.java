package com.zjh.router.compiler;

public interface ProcessorConfig {

    //Router注解 包名 + 类名
    String ROUTER_PACKAGE = "com.zjh.router.annotation.Router";
    //Parameter注解 包名 + 类名
    String PARAMETER_PACKAGE = "com.zjh.router.annotation.Parameter";
    String OPTIONS = "moduleName";
    String APT_PACKAGE = "packageNameForAPT";

    String ROUTER_API_PACKAGE = "com.zjh.router.api";
    String ROUTER_API_GROUP = ROUTER_API_PACKAGE + ".RouterGroup";
    String GROUP_METHOD_NAME = "getGroupMap";
    String GROUP_VAR1 = "groupMap";
    String GROUP_FILE_NAME = "Router$$Group$$";
    String ROUTER_API_PATH = ROUTER_API_PACKAGE + ".RouterPath";
    String PATH_METHOD_NAME = "getPathMap";
    String PATH_VAR1 = "pathMap";
    String PATH_FILE_NAME = "Router$$Path$$";
    String ROUTER_API_PARAMETER = ROUTER_API_PACKAGE + ".IParameter";
    String PARAMETER_NAME = "targetParameter";
    String PARAMETER_METHOD_NAME = "getParameter";
    String PARAMETER_FILE_NAME = "$$Parameter";
    String CALL = ROUTER_API_PACKAGE + ".Call";
    String ROUTER_MANAGER = "RouterManager";

    String STRING = "java.lang.String";
    String ACTIVITY = "android.app.Activity";
}
