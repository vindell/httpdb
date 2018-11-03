/*
 * Copyright (c) 2010-2020, wandalong (hnxyhcwdl1003@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.httpdb.io;

import java.io.IOException;
import java.io.InputStream;

public final class CountdownInputStream extends InputStream {

    private long        m_count;
    private InputStream m_input;

    public CountdownInputStream(final InputStream is) {
        m_input = is;
    }

    public int read() throws IOException {

        if (m_count <= 0) {
            return -1;
        }

        final int b = m_input.read();

        if (b >= 0) {
            m_count--;
        }

        return b;
    }

    public int read(final byte[] buf) throws IOException {

        if (buf == null) {
            throw new NullPointerException();
        } 

        if (m_count <= 0) {
            return -1;
        }

        int len = buf.length;

        if (len > m_count) {
            len = (int) m_count;
        }

        final int r = m_input.read(buf, 0, len);

        if (r > 0) {
            m_count -= r;
        }

        return r;
    }

    public int read(final byte[] buf, final int off,
                    int len) throws IOException {

        if (buf == null) {
            throw new NullPointerException();
        } 

        if (m_count <= 0) {
            return -1;
        }

        if (len > m_count) {
            len = (int) m_count;
        }

        final int r = m_input.read(buf, off, len);

        if (r > 0) {
            m_count -= r;
        }

        return r;
    }

    public void close() throws IOException {
        m_input.close();
    }

    public int available() throws IOException {
        return Math.min(m_input.available(),
                        (int) Math.min(Integer.MAX_VALUE, m_count));
    }

    public long skip(long count) throws IOException {
        return (count <= 0) ? 0
                            : m_input.skip(Math.min(m_count, count));
    }

    public long getCount() {
        return m_count;
    }

    public void setCount(long count) {
        m_count = count;
    }
}

