/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.scrplugin.tags.annotation.sling;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.felix.scrplugin.Constants;
import org.apache.felix.scrplugin.tags.JavaClassDescription;
import org.apache.felix.scrplugin.tags.annotation.defaulttag.AbstractTag;

/**
 * Description of a java tag for components.
 */
public class SlingServletServiceTag extends AbstractTag {

    /**
     * @param desc Description
     */
    public SlingServletServiceTag(JavaClassDescription desc) {
        super(desc, null);
    }

    @Override
    public String getName() {
        return Constants.SERVICE;
    }

    @Override
    public Map<String, String> createNamedParameterMap() {
        final Map<String, String> map = new HashMap<String, String>();

        map.put(Constants.SERVICE_INTERFACE, Servlet.class.getName());

        return map;
    }

}
