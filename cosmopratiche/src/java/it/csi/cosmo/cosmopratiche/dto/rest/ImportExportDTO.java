/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.List;
import it.csi.cosmo.common.exception.ManagedException;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.DryRunExitException.FieldConflictResolutionInput;

public class ImportExportDTO {

  private ImportExportDTO() {
    // NOP
  }

  public enum LivelloMessaggio {
    DEBUG, INFO, SUCCESS, WARNING, DANGER
  }

  public static class MessaggioImportazione {
    protected LivelloMessaggio livello;
    protected String messaggio;

    public LivelloMessaggio getLivello() {
      return livello;
    }

    public void setLivello(LivelloMessaggio livello) {
      this.livello = livello;
    }

    public String getMessaggio() {
      return messaggio;
    }

    public void setMessaggio(String messaggio) {
      this.messaggio = messaggio;
    }

    public MessaggioImportazione(LivelloMessaggio livello, String messaggio) {
      super();
      this.livello = livello;
      this.messaggio = messaggio;
    }

  }

  public static class EsportaTipoPraticaRequest {
    private String tenantId;
    private String codiceTipoPratica;

    public String getTenantId() {
      return tenantId;
    }

    public void setTenantId(String tenantId) {
      this.tenantId = tenantId;
    }

    public String getCodiceTipoPratica() {
      return codiceTipoPratica;
    }

    public void setCodiceTipoPratica(String codiceTipoPratica) {
      this.codiceTipoPratica = codiceTipoPratica;
    }
  }

  public static class ImportaTipoPraticaRequest {
    private String tenantId;
    private String sourceContent;
    private List<FieldConflictResolutionInput> conflictResolutionInput;
    private Boolean preview;

    public String getTenantId() {
      return tenantId;
    }

    public void setTenantId(String tenantId) {
      this.tenantId = tenantId;
    }

    public String getSourceContent() {
      return sourceContent;
    }

    public void setSourceContent(String sourceContent) {
      this.sourceContent = sourceContent;
    }

    public List<FieldConflictResolutionInput> getConflictResolutionInput() {
      return conflictResolutionInput;
    }

    public void setConflictResolutionInput(
        List<FieldConflictResolutionInput> conflictResolutionInput) {
      this.conflictResolutionInput = conflictResolutionInput;
    }

    public Boolean getPreview() {
      return preview;
    }

    public void setPreview(Boolean preview) {
      this.preview = preview;
    }
  }

  public static class DryRunExitException extends ManagedException {

    public enum FieldConflictResolutionInputAction {
      IGNORE, OVERWRITE
    }

    public static class FieldConflictResolutionInput {

      protected Boolean automatic;

      protected String fullKey;

      protected FieldConflictResolutionInputAction action;

      protected String acceptedValue;

      public Boolean getAutomatic() {
        return automatic;
      }

      public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
      }

      public String getFullKey() {
        return fullKey;
      }

      public void setFullKey(String fullKey) {
        this.fullKey = fullKey;
      }

      public FieldConflictResolutionInputAction getAction() {
        return action;
      }

      public void setAction(FieldConflictResolutionInputAction action) {
        this.action = action;
      }

      public String getAcceptedValue() {
        return acceptedValue;
      }

      public void setAcceptedValue(String acceptedValue) {
        this.acceptedValue = acceptedValue;
      }

    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Object executionResult;

    public DryRunExitException(Object executionResult) {
      super(200, "OK", "done", null, executionResult);
      this.executionResult = executionResult;
    }

    public static long getSerialversionuid() {
      return serialVersionUID;
    }

    public Object getExecutionResult() {
      return executionResult;
    }
  }
}
