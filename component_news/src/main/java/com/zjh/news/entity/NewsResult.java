package com.zjh.news.entity;

import java.util.List;

public class NewsResult {

        private String stat;
        private String page;
        private String pagesize;
        private List<NewsData> data;

        public String getStat() {
                return stat;
        }

        public void setStat(String stat) {
                this.stat = stat;
        }

        public String getPage() {
                return page;
        }

        public void setPage(String page) {
                this.page = page;
        }

        public String getPagesize() {
                return pagesize;
        }

        public void setPagesize(String pagesize) {
                this.pagesize = pagesize;
        }

        public List<NewsData> getData() {
                return data;
        }

        public void setData(List<NewsData> data) {
                this.data = data;
        }
}
