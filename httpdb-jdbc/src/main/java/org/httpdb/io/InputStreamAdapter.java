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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamAdapter extends InputStream {

    private final CountdownInputStream m_countdownInputStream;

    @SuppressWarnings("unused")
    public InputStreamAdapter(final File file, final long pos,
                       final long length)
                       throws FileNotFoundException, IOException {

        if (file == null) {
            throw new NullPointerException("file");
        }

        if (pos < 0) {
            throw new IllegalArgumentException("pos: " + pos);
        }

        if (length < 0) {
            throw new IllegalArgumentException("length: " + length);
        }

        final FileInputStream fis = new FileInputStream(file);

        if (pos > 0) {
            long actualPos = fis.skip(pos);
        }

        final BufferedInputStream  bis = new BufferedInputStream(fis);
        final CountdownInputStream cis = new CountdownInputStream(bis);

        cis.setCount(length);

        m_countdownInputStream = cis;
    }

    public int available() throws IOException {
        return m_countdownInputStream.available();
    }

    public int read() throws IOException {
        return m_countdownInputStream.read();
    }

    public int read(byte[] b) throws IOException {
        return m_countdownInputStream.read(b);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return m_countdownInputStream.read(b, off, len);
    }

    public long skip(long n) throws IOException {
        return m_countdownInputStream.skip(n);
    }

    public void close() throws IOException {
        m_countdownInputStream.close();
    }
}