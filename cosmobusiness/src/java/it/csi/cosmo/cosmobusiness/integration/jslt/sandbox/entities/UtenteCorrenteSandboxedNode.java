/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.entities;

import java.util.List;
import it.csi.cosmo.common.security.model.GruppoDTO;
import it.csi.cosmo.common.security.model.ProfiloDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.AbstractJsltSandboxedNode;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmobusiness.util.listener.SpringApplicationContextHelper;

/**
 *
 */

public class UtenteCorrenteSandboxedNode extends AbstractJsltSandboxedNode {

  private UserInfoDTO entity;

  public UtenteCorrenteSandboxedNode(JsltProviderContext ctx, UserInfoDTO entity) {
    super(ctx);
    this.entity = entity;
  }

  public Long getId() {
    return entity.getId();
  }

  public Boolean getAnonimo() {
    return entity.getAnonimo();
  }

  public String getNome() {
    return entity.getNome();
  }

  public String getCognome() {
    return entity.getCognome();
  }

  public String getCodiceFiscale() {
    return entity.getCodiceFiscale();
  }

  public String getEmail() {
    return entity.getEmail();
  }

  public String getTelefono() {
    return entity.getTelefono();
  }

  public ProfiloDTO getProfilo() {
    return entity.getProfilo();
  }

  public List<GruppoDTO> getGruppi() {
    return entity.getGruppi();
  }

  public EnteSandboxedNode getEnte() {
    if (entity.getEnte() == null) {
      return null;
    }
    CosmoTEnteRepository repo = SpringApplicationContextHelper.getBean(CosmoTEnteRepository.class);
    return new EnteSandboxedNode(this.context, repo.findOne(entity.getEnte().getId()));
  }

}
