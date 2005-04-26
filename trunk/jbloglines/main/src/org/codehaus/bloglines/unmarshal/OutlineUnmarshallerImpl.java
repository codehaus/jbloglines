/*
 * 
 * 
 * Copyright (c) Zohar Melamed & William Jones - All rights reserved.
 * 
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. Due credit should be given to The Codehaus and Contributors
 * http://jbloglines.codehaus.org/
 * 
 * THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * 
 *  
 */
package org.codehaus.bloglines.unmarshal;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.codehaus.bloglines.Outline;

import java.util.ArrayList;
import java.util.List;

public class OutlineUnmarshallerImpl implements OutlineUnmarshaller {
    private XStream xstream;

    public OutlineUnmarshallerImpl() {
        xstream = new XStream();
        xstream.alias("outline", Outline.class);
        xstream.registerConverter(new OutlineConverter());
    }

    public Outline unmarshal(String outlines) {
        return (Outline) xstream.fromXML(outlines.substring(outlines.indexOf("<outline"), outlines.length()));
    }

    static class OutlineConverter implements Converter {
        public boolean canConvert(Class type) {
            return type == Outline.class;
        }

        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
            throw new UnsupportedOperationException();
        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            Outline currentOutline = populateOutline(reader);
            List children = new ArrayList();
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                Outline child = (Outline) unmarshal(reader, context);
                children.add(child);
            }

            if (!children.isEmpty()) {
                Outline[] newChildren = (Outline[]) children.toArray(new Outline[children.size()]);
                currentOutline.setChildren(newChildren);
            }

            reader.moveUp();
            return currentOutline;
        }

        private Outline populateOutline(HierarchicalStreamReader reader) {
            Outline outline = new Outline();
            outline.setTitle(reader.getAttribute("title"));
            outline.setType(reader.getAttribute("type"));
            outline.setHtmlUrl(reader.getAttribute("htmlUrl"));
            outline.setXmlUrl(reader.getAttribute("xmlUrl"));
            outline.setSubscriptionId(reader.getAttribute("BloglinesSubId"));
            outline.setIgnore("1".equals(reader.getAttribute("BloglinesIgnore")));
            String unread = reader.getAttribute("BloglinesUnread");
            if (unread != null) {
                outline.setUnread(Integer.parseInt(unread));
            }
            return outline;
        }
    }
}