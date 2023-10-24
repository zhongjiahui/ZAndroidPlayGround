package com.zjh.router.annotation.bean;

import javax.lang.model.element.Element;

public class RouterBean {

    public enum TypeEnum{
        ACTIVITY,
        CALL
    }

    private TypeEnum typeEnum;
    private Element element;
    private Class<?> mClass;
    private String path;
    private String group;

    public TypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(TypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Class<?> getmClass() {
        return mClass;
    }

    public void setmClass(Class<?> mClass) {
        this.mClass = mClass;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public RouterBean(TypeEnum typeEnum, Element element, Class<?> mClass, String path, String group) {
        this.typeEnum = typeEnum;
        this.element = element;
        this.mClass = mClass;
        this.path = path;
        this.group = group;
    }

    public RouterBean(TypeEnum typeEnum, Class<?> mClass, String path, String group) {
        this.typeEnum = typeEnum;
        this.mClass = mClass;
        this.path = path;
        this.group = group;
    }

    public static RouterBean create(TypeEnum typeEnum, Class<?> clazz, String path, String group){
        return new RouterBean(typeEnum, clazz, path, group);
    }

    private RouterBean(Builder builder){
        this.typeEnum = builder.typeEnum;
        this.element = builder.element;
        this.mClass = builder.mClass;
        this.path = builder.path;
        this.group = builder.group;
    }

    public static class Builder{

        private TypeEnum typeEnum;
        private Element element;
        private Class<?> mClass;
        private String path;
        private String group;

        public Builder addType(TypeEnum typeEnum){
            this.typeEnum = typeEnum;
            return this;
        }

        public Builder addElement(Element element){
            this.element = element;
            return this;
        }

        public Builder addClass(Class<?> clazz){
            this.mClass = clazz;
            return this;
        }

        public Builder addPath(String path){
            this.path = path;
            return this;
        }

        public Builder addGroup(String group){
            this.group = group;
            return this;
        }

        public RouterBean build(){
            if(path == null || path.length() == 0){
                throw new IllegalArgumentException("path必填项为空");
            }
            return new RouterBean(this);
        }
    }

    @Override
    public String toString() {
        return "RouterBean{" + "path = " + path + " group = " + group+"}";
    }
}
