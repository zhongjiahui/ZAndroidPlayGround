package com.zjh.news.entity;

public class NewsEntity {

        private String resultcode;
        private String reason;
        private NewsResult result;
        private String error_code;

        public String getResultcode() {
                return resultcode;
        }

        public void setResultcode(String resultcode) {
                this.resultcode = resultcode;
        }

        public String getReason() {
                return reason;
        }

        public void setReason(String reason) {
                this.reason = reason;
        }

        public NewsResult getResult() {
                return result;
        }

        public void setResult(NewsResult result) {
                this.result = result;
        }

        public String getError_code() {
                return error_code;
        }

        public void setError_code(String error_code) {
                this.error_code = error_code;
        }
}
