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

package com.basho.riak.client.api.commands.indexes;

import com.basho.riak.client.core.StreamingRiakFuture;
import com.basho.riak.client.core.operations.SecondaryIndexQueryOperation;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.util.BinaryValue;

/**
 * Performs a 2i query where the 2i index keys are raw bytes.
 * <script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
 * <p>
 * A RawIndexQuery is used when you are using raw bytes for your 2i keys. The
 * parameters are provided as BinaryValue objects.
 * </p>
 * <pre class="prettyprint">
 * {@code
 * byte[] bytes = new byte[] { 1,2,3,4};
 * BinaryValue key = BinaryValue.create(bytes);
 * Namespace ns = new Namespace("my_type", "my_bucket");
 * RawIndexQuery q = new RawIndexQuery.Builder(ns, "my_index", Type._BIN, key).build();
 * RawIndexquery.Response resp = client.execute(q);}</pre>
 *
 * <p>
 * You can also stream the results back before the operation is fully complete.
 * This reduces the time between executing the operation and seeing a result,
 * and reduces overall memory usage if the iterator is consumed quickly enough.
 * The result iterable can only be iterated once though.
 * If the thread is interrupted while the iterator is polling for more results,
 * a {@link RuntimeException} will be thrown.
 * <pre class="prettyprint">
 * {@code
 * byte[] bytes = new byte[] { 1,2,3,4};
 * BinaryValue key = BinaryValue.create(bytes);
 * Namespace ns = new Namespace("my_type", "my_bucket");
 * RawIndexQuery q = new RawIndexQuery.Builder(ns, "my_index", Type._BIN, key).build();
 * RiakFuture<RawIndexQuery.StreamingResponse, RawIndexQuery> streamingFuture =
 *     client.executeAsyncStreaming(q, 200);
 * RawIndexQuery.StreamingResponse streamingResponse = streamingFuture.get();
 *
 * for (RawIndexQuery.Response.Entry e : streamingResponse)
 * {
 *     System.out.println(e.getRiakObjectLocation().getKey().toString());
 * }
 * // Wait for the command to fully finish.
 * streamingFuture.await();
 * // The StreamingResponse will also contain the continuation, if the operation returned one.
 * streamingResponse.getContinuation(); }</pre>
 * </p>
 *
 * @author Brian Roach <roach at basho dot com>
 * @author Alex Moore <amoore at basho dot com>
 * @author Sergey Galkin <srggal at gmail dot com>
 * @since 2.0
 */
public class RawIndexQuery extends SecondaryIndexQuery<BinaryValue, RawIndexQuery.Response, RawIndexQuery>
{
    private final IndexConverter<BinaryValue> converter;

    protected RawIndexQuery(Init<BinaryValue,?> builder)
    {
        super(builder, Response::new, Response::new);
        this.converter = new IndexConverter<BinaryValue>()
        {
            @Override
            public BinaryValue convert(BinaryValue input)
            {
                return input;
            }
        };
    }

    @Override
    protected IndexConverter<BinaryValue> getConverter()
    {
        return converter;
    }

    /**
     * Builder used to construct a RawIndexQuery command.
     */
    public static class Builder extends SecondaryIndexQuery.Init<BinaryValue, Builder>
    {
        /**
         * Construct a Builder for a RawIndexQuery with a range.
         * <p>
         * Note that your index name should not include the Riak {@literal _int} or
         * {@literal _bin} extension as these are supplied by the type.
         * <p>
         * @param namespace The namespace in Riak to query.
         * @param indexName The index name in Riak to query.
         * @param type The Riak index type.
         * @param start The start of the 2i range.
         * @param end The end of the 2i range.
         */
        public Builder(Namespace namespace, String indexName, Type type, BinaryValue start, BinaryValue end)
        {
            super(namespace, indexName + type, start, end);
        }

        /**
         * Construct a Builder for a RawIndexQuery with a single 2i key.
         * <p>
         * Note that your index name should not include the Riak {@literal _int} or
         * {@literal _bin} extension as these are supplied by the type.
         * <p>
         * @param namespace The namespace in Riak to query.
         * @param indexName The index name in Riak to query.
         * @param type The Riak index type.
         * @param match The 2i key to query.
         */
        public Builder(Namespace namespace, String indexName, Type type, BinaryValue match)
        {
            super(namespace, indexName + type, match);
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        /**
         * Construct the query.
         * @return a new RawIndexQuery
         */
        public RawIndexQuery build()
        {
            return new RawIndexQuery(this);
        }
    }

    public static class Response extends SecondaryIndexQuery.Response<BinaryValue, SecondaryIndexQuery.Response.Entry<BinaryValue>>
    {
        Response(Namespace queryLocation, IndexConverter<BinaryValue> converter, int timeout, StreamingRiakFuture<SecondaryIndexQueryOperation.Response, SecondaryIndexQueryOperation.Query> coreFuture)
        {
            super(queryLocation, converter, timeout, coreFuture);
        }

        protected Response(Namespace queryLocation, SecondaryIndexQueryOperation.Response coreResponse, IndexConverter<BinaryValue> converter)
        {
            super(queryLocation, coreResponse, converter);
        }
    }
}
