/*
 * Copyright 2013 Basho Technologies Inc
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

package com.basho.riak.client.api.commands.search;

import com.basho.riak.client.api.AsIsRiakCommand;
import com.basho.riak.client.core.operations.YzFetchIndexOperation;

/**
 * Command used to fetch a search index from Riak.
 * @author Dave Rusek <drusek at basho dot com>
 * @since 2.0
 */
public class FetchIndex extends AsIsRiakCommand<YzFetchIndexOperation.Response, String>
{
    private final String index;

    FetchIndex(Builder builder)
    {
        this.index = builder.index;
    }

    @Override
    protected YzFetchIndexOperation buildCoreOperation()
    {
        return new YzFetchIndexOperation.Builder().withIndexName(index).build();
    }

    /**
     * Builder for a FetchIndex command.
     */
    public static class Builder
    {
        private final String index;

        /**
         * Construct a Builder for a FetchIndex command.
         *
         * @param index The name of the search index to fetch from Riak.
         */
        public Builder(String index)
        {
            this.index = index;
        }

        /**
         * Construct the FetchIndex command.
         * @return the new FetchIndex command.
         */
        public FetchIndex build()
        {
            return new FetchIndex(this);
        }
    }
}
