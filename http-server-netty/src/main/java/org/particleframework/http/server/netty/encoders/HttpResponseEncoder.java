/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.http.server.netty.encoders;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.particleframework.core.naming.Named;
import org.particleframework.core.order.Ordered;
import org.particleframework.http.HttpResponse;
import org.particleframework.http.server.netty.NettyHttpRequest;
import org.particleframework.http.server.netty.NettyHttpResponse;

import javax.inject.Singleton;
import java.util.List;

/**
 * Encodes an {@link HttpResponse} to a {@link io.netty.handler.codec.http.FullHttpResponse}
 *
 * @author Graeme Rocher
 * @since 1.0
 */
@ChannelHandler.Sharable
@Singleton
public class HttpResponseEncoder extends MessageToMessageEncoder<HttpResponse> implements Ordered, Named {

    public static final int ORDER = Ordered.LOWEST_PRECEDENCE - 1000;
    public static final String NAME = "http-response-encoder";

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpResponse msg, List<Object> out) throws Exception {
        NettyHttpResponse res = NettyHttpResponse.getOrCreate(NettyHttpRequest.current(ctx));
        out.add(res.getNativeResponse());
    }

    @Override
    public String getName() {
        return NAME;
    }
}