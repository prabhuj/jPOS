/*
 * Copyright (c) 2000 jPOS.org.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *    "This product includes software developed by the jPOS project 
 *    (http://www.jpos.org/)". Alternately, this acknowledgment may 
 *    appear in the software itself, if and wherever such third-party 
 *    acknowledgments normally appear.
 *
 * 4. The names "jPOS" and "jPOS.org" must not be used to endorse 
 *    or promote products derived from this software without prior 
 *    written permission. For written permission, please contact 
 *    license@jpos.org.
 *
 * 5. Products derived from this software may not be called "jPOS",
 *    nor may "jPOS" appear in their name, without prior written
 *    permission of the jPOS project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  
 * IN NO EVENT SHALL THE JPOS PROJECT OR ITS CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS 
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING 
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the jPOS Project.  For more
 * information please see <http://www.jpos.org/>.
 */

package org.jpos.space;

import java.util.StringTokenizer;

/**
 * Creates a space based on a space URI.
 *
 * <p>A space URI has three parts:
 *  <ul>
 *   <li>scheme
 *   <li>name
 *   <li>optional parameter
 *  </ul>
 * <p> 
 * <p>
 *
 * Examples:
 *
 * <pre>
 *   // default unnamed space
 *   Space sp = SpaceFactory.getSpace (); 
 *
 *   // transient space named "test"
 *   Space sp = SpaceFactory.getSpace ("transient:test");  
 *
 *   // persistent space named "test"
 *   Space sp = SpaceFactory.getSpace ("persistent:test"); 
 *
 *   // jdbm space named test
 *   Space sp = SpaceFactory.getSpace ("jdbm:test");
 *
 *   // jdbm space named test, storage located in /tmp/test
 *   Space sp = SpaceFactory.getSpace ("jdbm:test:/tmp/test");  
 * </pre>
 *
 */
public class SpaceFactory {
    public static final String TRANSIENT  = "transient";
    public static final String PERSISTENT = "persistent";
    public static final String JDBM       = "jdbm";

    /**
     * @return the default TransientSpace
     */
    public static Space getSpace () {
        return getSpace (TRANSIENT);
    }

    /**
     * @param spaceUri 
     * @return Space for given URI or null
     */
    public static Space getSpace (String spaceUri) {
        if (spaceUri == null)
            return getSpace ();

        Space sp = null;
        String scheme = null;
        String name   = null;
        String param  = null;

        StringTokenizer st = new StringTokenizer (spaceUri, ":");
        int count = st.countTokens();
        if (count == 0) {
            scheme = TRANSIENT;
        }
        else if (count == 1) {
            scheme = TRANSIENT;
            name   = st.nextToken ();
        }
        else {
            scheme = st.nextToken ();
            name   = st.nextToken ();
        }
        if (st.hasMoreTokens()) {
            param  = st.nextToken ();
        }
        if (TRANSIENT.equals (scheme)) {
            if (name != null)
                sp = TransientSpace.getSpace (name);
            else
                sp = TransientSpace.getSpace ();
        } else if (PERSISTENT.equals (scheme)) {
            if (name != null)
                sp = PersistentSpace.getSpace (name);
            else
                sp = PersistentSpace.getSpace ();
        } else if (JDBM.equals (scheme)) {
            if (name != null && param != null)
                sp = JDBMSpace.getSpace (name, param);
            else if (name != null) 
                sp = JDBMSpace.getSpace (name);
            else
                sp = JDBMSpace.getSpace ();
        }
        if (sp == null)
            throw new SpaceError ("Invalid space URI: " + spaceUri);
        return sp;
    }
}
