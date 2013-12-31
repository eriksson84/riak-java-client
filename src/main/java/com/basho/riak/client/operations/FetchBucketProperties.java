package com.basho.riak.client.operations;

import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.operations.FetchBucketPropsOperation;
import com.basho.riak.client.query.BucketProperties;
import com.basho.riak.client.util.ByteArrayWrapper;

import java.util.concurrent.ExecutionException;

public class FetchBucketProperties extends RiakCommand<BucketProperties>
{

	private final Location location;

	public FetchBucketProperties(Builder builder)
	{
		this.location = builder.location;
	}

	@Override
	BucketProperties execute(RiakCluster cluster) throws ExecutionException, InterruptedException
	{
		ByteArrayWrapper wrappedBucket = ByteArrayWrapper.create(location.getBucket());
		FetchBucketPropsOperation.Builder operation = new FetchBucketPropsOperation.Builder(wrappedBucket);

		if (location.hasType())
		{
			operation.withBucketType(ByteArrayWrapper.create(location.getType()));
		}

		return cluster.execute(operation.build()).get();
	}

	public static class Builder
	{

		private final Location location;

		public Builder(Location location)
		{
			this.location = location;
		}

		public FetchBucketProperties build()
		{
			return new FetchBucketProperties(this);
		}
	}

}
