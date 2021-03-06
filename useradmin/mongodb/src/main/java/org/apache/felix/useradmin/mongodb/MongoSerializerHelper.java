/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.felix.useradmin.mongodb;

import com.mongodb.MongoException;
import org.apache.felix.useradmin.RoleFactory;
import org.bson.Document;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

/**
 * Provides a helper class for (de)serializing data to/from MongoDB.
 */
final class MongoSerializerHelper {

    static final String TYPE = "type";
    static final String NAME = "name";

    static final String PROPERTIES = "properties";
    static final String CREDENTIALS = "credentials";
    static final String MEMBERS = "members";
    static final String REQUIRED_MEMBERS = "requiredMembers";

    static final String SET = "$set";

    private final RoleProvider m_roleProvider;

    /**
     * Creates a new {@link MongoSerializerHelper} instance.
     *
     * @param roleProvider the role provider to use, cannot be <code>null</code>.
     */
    public MongoSerializerHelper(RoleProvider roleProvider) {
        m_roleProvider = roleProvider;
    }

    /**
     * Converts a given {@link Document} to a {@link Role} instance.
     *
     * @param object the {@link Document} to convert, cannot be <code>null</code>.
     * @return a {@link Role} instance, never <code>null</code>.
     */
    public Role deserialize(Document object) {
        int type = ((Integer) object.get(TYPE)).intValue();
        String name = (String) object.get(NAME);

        Role result = RoleFactory.createRole(type, name);
        // Read the generic properties of the role...
        deserializeDictionary(result.getProperties(), (Document) object.get(PROPERTIES));

        if ((Role.GROUP == type) || (Role.USER == type)) {
            // This is safe, as Group extends from User...
            deserializeDictionary(((User) result).getCredentials(), (Document) object.get(CREDENTIALS));

            if (Role.GROUP == type) {
                for (Role member : getRoles((List) object.get(MEMBERS))) {
                    ((Group) result).addMember(member);
                }

                for (Role member : getRoles((List) object.get(REQUIRED_MEMBERS))) {
                    ((Group) result).addRequiredMember(member);
                }
            }
        }

        return result;
    }

    /**
     * Serializes the given {@link Role} to a {@link Document} instance.
     *
     * @param role the {@link Role} to serialize, cannot be <code>null</code> (unchecked!).
     * @return a {@link Document} representing the given {@link Role}, never <code>null</code>.
     */
    public Document serialize(Role role) {
        Document data = new Document();

        int type = role.getType();

        data.put(TYPE, type);
        data.put(NAME, role.getName());

        data.put(PROPERTIES, serializeDictionary(role.getProperties()));
        if ((Role.GROUP == type) || (Role.USER == type)) {
            data.put(CREDENTIALS, serializeDictionary(((User) role).getCredentials()));

            if (Role.GROUP == type) {
                data.put(MEMBERS, getRoleNames(((Group) role).getMembers()));
                data.put(REQUIRED_MEMBERS, getRoleNames(((Group) role).getRequiredMembers()));
            }
        }

        return data;
    }

    /**
     * Creates a {@link Document} with the given role and name.
     *
     * @param roleName the name of the role to serialize, cannot be <code>null</code> or empty (unchecked!);
     * @param type the type of the role to serialize.
     * @return a {@link Document} representing the role with the given name and type, never <code>null</code>.
     */
    public Document serialize(String roleName, int type) {
        Document data = new Document();

        data.put(TYPE, type);
        data.put(NAME, roleName);

        return data;
    }

    /**
     * Creates a serialized version of the given {@link Role} to be used in an update statement.
     *
     * @param role the {@link Role} to update, cannot be <code>null</code>.
     * @return a {@link Document} representing an update statement for the given {@link Role}.
     */
    public Document serializeUpdate(Role role) {
        int type = role.getType();

        Document changeSet = new Document();
        changeSet.put(PROPERTIES, serializeDictionary(role.getProperties()));

        if (Role.USER == type) {
            changeSet.put(CREDENTIALS, serializeDictionary(((User) role).getCredentials()));
        }

        if (Role.GROUP == type) {
            Group group = (Group) role;
            changeSet.put(MEMBERS, getRoleNames(group.getMembers()));
            changeSet.put(REQUIRED_MEMBERS, getRoleNames(group.getRequiredMembers()));
        }

        return new Document(SET, changeSet);
    }

    /**
     * Finds an existing member by its name.
     *
     * @param name the name of the member to return, cannot be <code>null</code>.
     * @return a member instance, never <code>null</code>.
     * @throws MongoException in case the requested member was not found (or any other MongoDB exception).
     */
    final Role findExistingMember(String name) {
        Role result = m_roleProvider.getRole(name);
        if (result == null) {
            throw new MongoException("No such role: " + name);
        }
        return result;
    }

    /**
     * Deserializes the given {@link Document} into the given {@link Dictionary}.
     *
     * @param dictionary the dictionary to fill, cannot be <code>null</code>;
     * @param object the {@link Document} to deserialize, can be <code>null</code>.
     */
    private void deserializeDictionary(Dictionary dictionary, Document object) {
        // FELIX-4399: MongoDB does return null for empty properties...
        if (object != null) {
            for (String key : object.keySet()) {
                dictionary.put(KeyCodec.decode(key), object.get(key));
            }
        }
    }

    /**
     * Serializes a given array of {@link Role}s to an list for storing in a {@link Document}.
     *
     * @param members the {@link Role}s to serialize, cannot be <code>null</code>.
     * @return the "serialized" array, never <code>null</code>.
     */
    private List<String> getRoleNames(Role[] members) {
        List<String> result = new ArrayList<String>();
        if (members != null) {
            for (Role member : members) {
                result.add(member.getName());
            }
        }
        return result;
    }

    /**
     * Returns all roles mentioned in the given list.
     *
     * @param list the list with role names to convert, can be <code>null</code>.
     * @return a list with {@link Role}s, never <code>null</code>.
     */
    private List<Role> getRoles(List list) {
        List<Role> result = new ArrayList<Role>();
        // FELIX-4399: MongoDB does return null for empty properties...
        int size = (list == null) ? 0 : list.size();
        for (int i = 0; i < size; i++) {
            final String memberName = (String) list.get(i);
            result.add(findExistingMember(memberName));
        }
        return result;
    }

    /**
     * Serializes a given {@link Dictionary} into a {@link Document}.
     *
     * @param properties the {@link Dictionary} to serialize, cannot be <code>null</code>.
     * @return the serialized dictionary, never <code>null</code>.
     */
    private Document serializeDictionary(Dictionary properties) {
        Document result = new Document();

        Enumeration<String> keysEnum = properties.keys();
        while (keysEnum.hasMoreElements()) {
            String key = keysEnum.nextElement();
            Object value = properties.get(key);

            result.append(KeyCodec.encode(key), value);
        }

        return result;
    }
}
