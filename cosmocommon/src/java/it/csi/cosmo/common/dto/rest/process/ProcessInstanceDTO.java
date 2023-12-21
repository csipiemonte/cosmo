/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.rest.process;

import java.util.Map;

/**
 *
 */

/**
 *
 *
 */
public class ProcessInstanceDTO {

  private String businessKey;
  private String tenantId;
  private Map<String, Object> variables;
  private String processDefinitionKey;
  private String id;

  public ProcessInstanceDTO() {
    // Auto-generated constructor stub
  }

  /**
   * @return the businessKey
   */
  public String getBusinessKey() {
    return businessKey;
  }

  /**
   * @param businessKey the businessKey to set
   */
  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }

  /**
   * @return the tenantId
   */
  public String getTenantId() {
    return tenantId;
  }

  /**
   * @param tenantId the tenantId to set
   */
  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * @return the variables
   */
  public Map<String, Object> getVariables() {
    return variables;
  }

  /**
   * @param variables the variables to set
   */
  public void setVariables(Map<String, Object> variables) {
    this.variables = variables;
  }

  /**
   * @return the processDefinitionKey
   */
  public String getProcessDefinitionKey() {
    return processDefinitionKey;
  }

  /**
   * @param processDefinitionKey the processDefinitionKey to set
   */
  public void setProcessDefinitionKey(String processDefinitionKey) {
    this.processDefinitionKey = processDefinitionKey;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "ProcessInstance [businessKey=" + businessKey + ", tenantId=" + tenantId + ", variables="
        + variables + ", processDefinitionKey=" + processDefinitionKey + ", id=" + id + "]";
  }


}
