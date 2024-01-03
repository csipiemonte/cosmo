/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.config;

import java.io.IOException;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DateSerializer extends StdSerializer<OffsetDateTime>{

 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected DateSerializer(Class<?> t, boolean dummy) {
		super(t, dummy);
	}

	@Override
	public void serialize(OffsetDateTime data, JsonGenerator json, SerializerProvider provider) throws IOException {
			if (data != null ) {
				json.writeString(data.toInstant().toString());
			}
		
	}

	
}
