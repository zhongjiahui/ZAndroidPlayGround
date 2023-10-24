package com.zjh.router.api;

import com.zjh.router.annotation.bean.RouterBean;

import java.util.Map;

public interface RouterPath {

    Map<String, RouterBean> getPathMap();
}
