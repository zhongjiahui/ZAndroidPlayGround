package com.zjh.retrofit;

/**
 * @author Administrator
 * @date 2023/9/22
 * @description
 **/
public abstract class ParameterHandler {

    abstract void apply(ServiceMethod serviceMethod, String value);

    static class QueryParameterHandler extends ParameterHandler{

        private final String key;

        public QueryParameterHandler(String key) {
            this.key = key;
        }

        @Override
        void apply(ServiceMethod serviceMethod, String value) {
                serviceMethod.addQueryParameter(key, value);
        }
    }

    static class FieldParameterHandler extends ParameterHandler{

        private final String key;

        public FieldParameterHandler(String key) {
            this.key = key;
        }

        @Override
        void apply(ServiceMethod serviceMethod, String value) {
            serviceMethod.addFieldParameter(key, value);
        }
    }
}
