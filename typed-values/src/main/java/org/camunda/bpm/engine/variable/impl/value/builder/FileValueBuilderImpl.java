/* Licensed under the Apache License, Version 2.0 (the "License");
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
package org.camunda.bpm.engine.variable.impl.value.builder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.camunda.bpm.engine.variable.impl.value.FileValueImpl;
import org.camunda.bpm.engine.variable.type.PrimitiveValueType;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.bpm.engine.variable.value.builder.FileValueBuilder;
import org.camunda.commons.utils.EnsureUtil;

/**
 * @author Ronny Bräunlich
 * @since 7.4
 *
 */
public class FileValueBuilderImpl implements FileValueBuilder {

  protected FileValueImpl fileValue;

  public FileValueBuilderImpl(String filename) {
    EnsureUtil.ensureNotNull("filename", filename);
    fileValue = new FileValueImpl(PrimitiveValueType.FILE, filename);
  }

  @Override
  public FileValue create() {
    return fileValue;
  }

  @Override
  public FileValueBuilder mimeType(String mimeType) {
    fileValue.setMimeType(mimeType);
    return this;
  }

  @Override
  public FileValueBuilder file(File file) throws IOException {
	  FileInputStream fin = null;
	  try {
		  fin = new FileInputStream(file);
		  
		  file(fin);
	  } finally {
		  org.camunda.commons.utils.IoUtil.closeSilently(fin);
	  }
	  return this;
  }

  @Override
  public FileValueBuilder file(InputStream is) throws IOException {

	  byte[] buffer = new byte[16 * 1024];

	  ByteArrayOutputStream os = new ByteArrayOutputStream();
      int read;
      
      try {
	      do {
	    	  read = is.read(buffer, 0, buffer.length);
	    	  if(read == -1) {
	    		  break;
	    	  }
			  os.write(buffer, 0, read);
	      } while(true);
	  
	      this.file(os.toByteArray());
      } finally {
		  org.camunda.commons.utils.IoUtil.closeSilently(is);
      }
    return this;
  }

  @Override
  public FileValueBuilder file(byte[] bytes) {
    fileValue.setValue(bytes);
    return this;
  }

  @Override
  public FileValueBuilder encoding(Charset encoding) {
    fileValue.setEncoding(encoding);
    return this;
  }

  @Override
  public FileValueBuilder encoding(String encoding) {
    fileValue.setEncoding(encoding);
    return this;
  }

}
