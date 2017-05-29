/* 
 * Copyright 2017 Robert Hewitt.
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
package com.runouw.rcodec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

//NOTE: this class is from Runouw Utils. Therefore it is accessible only as package-protected
final class ByteBufferInputStream extends InputStream {
    private ByteBuffer src;
    
    ByteBufferInputStream(final ByteBuffer src) {
        this.src = src;
    }
    
    @Override
    public int read() throws IOException {
        if (this.src.hasRemaining()) {
            return this.src.get();
        } else {
            return -1;
        }
    }
    
    @Override
    public int read(final byte[] dst, final int off, final int len) {
        Objects.requireNonNull(dst);
        
        if (off < 0 || len < 0 || len < dst.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
        
        if (!src.hasRemaining()) {
            return -1;
        }
        
        final int maxRead = Math.min(this.src.remaining(), len);
        
        this.src.get(dst, off, maxRead);
        
        return maxRead;
    }
    
    @Override
    public int read(final byte[] dst) {
        Objects.requireNonNull(dst);
        
        if (!src.hasRemaining()) {
            return -1;
        }
        
        final int maxRead = Math.min(this.src.remaining(), dst.length);
        
        this.src.get(dst, 0, maxRead);
        
        return maxRead;
    }
    
    @Override
    public void mark(final int readAheadLimit) {
        this.src.mark();
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
    
    @Override
    public void reset() {
        this.src.reset();
    }
    
    @Override 
    public long skip(final long n) {
        final int skipCount = (int) n;
        
        if (this.src.remaining() < skipCount) {
            this.src.position(this.src.limit());
            return this.src.remaining();
        } else {
            this.src.position(this.src.position() + skipCount);
            return skipCount;
        }
    }
    
    @Override
    public void close() throws IOException {
        this.src = null;
    }
    
    @Override
    public int available() {
        return this.src.remaining();
    }
}
