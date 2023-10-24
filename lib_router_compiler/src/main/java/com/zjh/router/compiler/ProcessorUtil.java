package com.zjh.router.compiler;

import java.util.Collection;
import java.util.Map;

public class ProcessorUtil {

    public static boolean isEmpty(CharSequence cs){
        return cs == null || cs.length() == 0;
    }

    public static boolean isEmpty(Collection<?> coll){
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map){
        return map == null || map.isEmpty();
    }
}
