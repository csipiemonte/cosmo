/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


/**
 * DTO che contiene un elenco di contenuti firmati insieme a un oggetto esito
 */

public class DosignSignedContentDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<DosignPayloadDTO> payloadList;

  private DosignOutcomeDTO esito;

  private DosignSignedContentDTO ( Builder builder ) {
    this.payloadList = builder.payloadList;
    this.esito = builder.esito;
  }

  /**
   * Creates builder to build {@link DosignSignedContentDTO}.
   *
   * @return created builder
   */
  public static Builder builder () {
    return new Builder ();
  }

  public List<DosignPayloadDTO> getPayloadList () {
    return payloadList;
  }

  public DosignOutcomeDTO getEsito () {
    return esito;
  }

  /**
   * Builder to build {@link DosignSignedContentDTO}.
   */
  public static final class Builder {

    private List<DosignPayloadDTO> payloadList = Collections.emptyList ();

    private DosignOutcomeDTO esito;

    private Builder () {
    }

    public Builder withPayloadList ( List<DosignPayloadDTO> payloadList ) {
      this.payloadList = payloadList;
      return this;
    }

    public Builder withEsito ( DosignOutcomeDTO esito ) {
      this.esito = esito;
      return this;
    }

    public DosignSignedContentDTO build () {
      return new DosignSignedContentDTO ( this );
    }
  }

}
