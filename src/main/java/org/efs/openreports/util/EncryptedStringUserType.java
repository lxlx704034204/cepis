/*
 * Copyright (C) 2005 Erik Swenson - erik@oreports.com
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *  
 */

package org.efs.openreports.util;

import java.io.Serializable;
import java.sql.*;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.*;

public class EncryptedStringUserType implements UserType {
	protected static Logger log = Logger
			.getLogger(EncryptedStringUserType.class);

	private static final int[] TYPES = new int[] { Types.VARCHAR };

	private Base64 bCodec = new Base64();

	public int[] sqlTypes() {
		return TYPES;
	}

	public Class returnedClass() {
		return String.class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y) {
			return true;
		}

		if (x == null) {
			return false;
		}

		return x.equals(y);
	}

	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor sessionImplementer, Object owner)
			throws HibernateException, SQLException {
		StringType stringType = new StringType();
		String encryptedValue = (String) stringType.nullSafeGet(rs, names[0],
				sessionImplementer);

		if (encryptedValue != null) {
			return decrypt(encryptedValue);
		} else {
			return null;
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement pStmt, Object value, int index,
			SessionImplementor sessionImplementer) throws HibernateException,
			SQLException {
		StringType stringType = new StringType();
		if (value != null) {
			String encryptedValue = encrypt((String) value);
			stringType.nullSafeSet(pStmt, encryptedValue, index,
					sessionImplementer);
		} else {
			stringType.nullSafeSet(pStmt, value, index, sessionImplementer);
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		if (value == null) {
			return null;
		} else {
			return new String((String) value);
		}
	}

	public boolean isMutable() {
		return false;
	}

	protected String encrypt(String value) {
		try {
			return (String) bCodec.encode(value);
		} catch (EncoderException ee) {
			log.warn("ENCRYPT - " + value + " : " + ee.getMessage());
			return value;
		}
	}

	protected String decrypt(String value) {
		//try {
			return (String) bCodec.decode(value).toString();
		/*} catch (DecoderException de) {
			log.warn("DECRYPT - " + value + " : " + de.getMessage());
			return value;
		}*/
	}

	public int hashCode(Object arg0) throws HibernateException {
		return arg0.hashCode();
	}

	public Object replace(Object arg0, Object arg1, Object arg2)
			throws HibernateException {
		return deepCopy(arg0);
	}

	public Object assemble(Serializable arg0, Object arg1)
			throws HibernateException {
		return deepCopy(arg0);
	}

	public Serializable disassemble(Object value) {
		return (Serializable) deepCopy(value);
	}

}