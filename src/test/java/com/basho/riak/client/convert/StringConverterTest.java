/*
 * Copyright 2014 Brian Roach <roach at basho dot com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.basho.riak.client.convert;

import com.basho.riak.client.query.Location;
import com.basho.riak.client.query.RiakObject;
import com.basho.riak.client.util.BinaryValue;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Brian Roach <roach at basho dot com>
 */
public class StringConverterTest
{
    
    @Test
    public void producesRiakObject()
    {
        String foo = "some value";
        Location loc = new Location("bucket").setKey("key");
        StringConverter converter = new StringConverter();
        
        
        Converter.OrmExtracted orm = converter.fromDomain(foo, loc);
        
        assertNotNull(orm.getRiakObject());
        RiakObject obj = orm.getRiakObject();
        assertEquals(foo, obj.getValue().toString());
        assertEquals("text/plain", obj.getContentType());
        assertEquals(loc, orm.getLocation());
    }
    
    @Test
    public void producesString()
    {
        String value = "some value";
        RiakObject obj = new RiakObject()
                        .setValue(BinaryValue.create(value));
        StringConverter converter = new StringConverter();
        
        String result = converter.toDomain(obj, null);
        
        assertEquals(value, result);
        
                        
    }
    
}
