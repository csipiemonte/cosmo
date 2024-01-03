/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.mapper;

import org.springframework.stereotype.Component;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoFruitore;

@Component
public class CosmoTFruitoreMapper {

  public RiferimentoFruitore toRiferimentoFruitore(CosmoTFruitore src) {
    if (src == null) {
      return null;
    }
    RiferimentoFruitore res = new RiferimentoFruitore();
    res.setId(src.getId() != null ? Long.parseLong(src.getId().toString()) : null);
    res.setNomeApp(src.getNomeApp());
    res.setApiManagerId(src.getApiManagerId());

    return res;
  }

  public CosmoTFruitore toCosmoTFruitore(RiferimentoFruitore src) {
		if(src == null) {
			return null ; 
		}
		CosmoTFruitore res = new CosmoTFruitore(); 
		res.setId( src.getId() != null ? (long)src.getId() : null);
		res.setNomeApp(src.getNomeApp());
        res.setApiManagerId(src.getApiManagerId());
		
		return res; 
	}
}
