package com.zjh.router.api;

import java.util.Map;

public interface RouterGroup {

    Map<String, Class<? extends RouterPath>> getGroupMap();
}
