/*
 * Copyright 2013 Brian Roach <roach at basho dot com>.
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

package com.basho.riak.client.operations;

import java.util.List;

/**
 *
 * @author Brian Roach <roach at basho dot com>
 */
public class DefaultResolver<T> implements ConflictResolver<T>
{

    @Override
    public T resolve(List<T> siblings)
    {
        if (siblings.size() > 1)
        {
            throw new IllegalStateException("Fetch returned siblings but no conflict resolver supplied");
        }
        else if (siblings.isEmpty())
        {
            return null;
        }
        else
        {
            return siblings.get(0);
        }
    }
    
}
