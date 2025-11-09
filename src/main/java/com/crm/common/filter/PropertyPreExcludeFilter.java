package com.crm.common.filter;

import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;

public class PropertyPreExcludeFilter extends SimplePropertyPreFilter {
    public PropertyPreExcludeFilter addExcludes(String... filters) {
       for(String filter:filters){
           this.getExcludes().add(filter);
       }
       return this;
   }
}
