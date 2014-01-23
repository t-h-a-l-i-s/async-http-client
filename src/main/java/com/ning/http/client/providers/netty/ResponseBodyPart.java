/*
 * Copyright 2010 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.ning.http.client.providers.netty;

import com.ning.http.client.AsyncHttpProvider;
import com.ning.http.client.HttpResponseBodyPart;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A callback class used when an HTTP response body is received.
 */
public class ResponseBodyPart extends HttpResponseBodyPart {

    private final ChannelBuffer content;
    private final AtomicReference<byte[]> bytes = new AtomicReference<byte[]>(null);
    private final boolean isLast;
    private boolean closeConnection = false;

    public ResponseBodyPart(URI uri, HttpResponse response, AsyncHttpProvider provider, boolean last) {
        this(uri, response, provider, null, last);
    }

    public ResponseBodyPart(URI uri, HttpResponse response, AsyncHttpProvider provider, HttpChunk chunk, boolean last) {
        super(uri, provider);
        content = chunk != null ? chunk.getContent() : response.getContent();
        isLast = last;
    }

    /**
     * Return the response body's part bytes received.
     *
     * @return the response body's part bytes received.
     */
    public byte[] getBodyPartBytes() {

        if (bytes.get() != null) {
            return bytes.get();
        }

        byte[] b = ChannelBufferUtil.channelBuffer2bytes(getChannelBuffer());
        bytes.set(b);
        return b;
    }

    public int writeTo(OutputStream outputStream) throws IOException {
        ChannelBuffer b = getChannelBuffer();
        int read = b.readableBytes();
        int index = b.readerIndex();
        if (read > 0) {
            b.readBytes(outputStream, read);
        }
        b.readerIndex(index);
        return read;
    }

    public ChannelBuffer getChannelBuffer() {
        return content;
    }

    @Override
    public ByteBuffer getBodyByteBuffer() {
        return ByteBuffer.wrap(getBodyPartBytes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLast() {
        return isLast;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markUnderlyingConnectionAsClosed() {
        closeConnection = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean closeUnderlyingConnection() {
        return closeConnection;
    }
}
