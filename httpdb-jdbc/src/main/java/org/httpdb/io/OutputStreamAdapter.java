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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class OutputStreamAdapter extends OutputStream {

    private final RandomAccessFile m_randomAccessFile;

    public OutputStreamAdapter(final File file,
                               final long pos)
                               throws FileNotFoundException, IOException {

        if (pos < 0) {
            throw new IllegalArgumentException("pos: " + pos);
        }

        m_randomAccessFile = new RandomAccessFile(file, "rw");

        m_randomAccessFile.seek(pos);
    }

    public void write(int b) throws IOException {
        m_randomAccessFile.write(b);
    }

    public void write(byte[] b) throws IOException {
        m_randomAccessFile.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        m_randomAccessFile.write(b, off, len);
    }

    public void flush() throws IOException {
        m_randomAccessFile.getFD().sync();
    }

    public void close() throws IOException {
        m_randomAccessFile.close();
    }
}
