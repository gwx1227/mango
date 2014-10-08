/*
 * Copyright 2014 mango.jfaster.org
 *
 * The Mango Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.jfaster.mango.operator;

import org.jfaster.mango.exception.NotReadableParameterException;
import org.jfaster.mango.util.reflect.ParameterDescriptor;
import org.jfaster.mango.util.reflect.Types;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author ash
 */
public class ParameterDescriptorContext {

    private final Map<String, ParameterDescriptor> parameterDescriptorMap = new HashMap<String, ParameterDescriptor>();
    private final List<ParameterDescriptor> parameterDescriptors = new LinkedList<ParameterDescriptor>();
    private final Map<String, Type> cache = new HashMap<String, Type>();

    public Type getPropertyType(String parameterName, String propertyPath) {
        String key = getCacheKey(parameterName, propertyPath);
        Type cachedType = cache.get(key);
        if (cachedType != null) { // 缓存命中，直接返回
            return cachedType;
        }
        ParameterDescriptor pd = parameterDescriptorMap.get(parameterName);
        if (pd == null ) {
            throw new NotReadableParameterException("parameter :" + parameterName + " is not readable");
        }
        Type parameterType = pd.getType();
        Type type = !propertyPath.isEmpty() ?
                Types.getPropertyType(parameterType, parameterName, propertyPath) :
                parameterType;
        cache.put(key, type);
        return type;
    }

    public List<ParameterDescriptor> getParameterDescriptors() {
        return parameterDescriptors;
    }

    public void addParameterDescriptor(String parameterName, ParameterDescriptor pd) {
        parameterDescriptorMap.put(parameterName, pd);
        parameterDescriptors.add(pd);
    }

    private String getCacheKey(String parameterName, String propertyPath) {
        return propertyPath.isEmpty() ? parameterName : parameterName + "." + propertyPath;
    }


}