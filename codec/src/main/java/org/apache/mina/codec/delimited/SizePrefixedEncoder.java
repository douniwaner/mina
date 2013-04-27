/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.mina.codec.delimited;

import java.nio.ByteBuffer;

import org.apache.mina.codec.StatelessProtocolEncoder;

/**
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 * 
 */
public class SizePrefixedEncoder<IN> implements StatelessProtocolEncoder<IN, ByteBuffer> {

    final private Transcoder<Integer, Integer> transcoder;

    public SizePrefixedEncoder(Transcoder<Integer, Integer> transcoder) {
        super();
        this.transcoder = transcoder;
    }

    @Override
    public Void createEncoderState() {
        // stateless!
        return null;
    }

    @Override
    public ByteBuffer encode(IN message, Void context) {
        ByteBuffer buffer;
        if (message instanceof ByteBuffer) {
            ByteBuffer messageBuffer = (ByteBuffer) message;

            buffer = ByteBuffer.allocate(transcoder.getEncodedSize(messageBuffer.remaining())
                    + messageBuffer.remaining());
            transcoder.writeTo(messageBuffer.remaining(), buffer);
            buffer.put(messageBuffer);
        } else {
            throw new RuntimeException("Message of type " + message.getClass() + " not handled");
        }
        buffer.flip();
        return buffer;
    }

}
