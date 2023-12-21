/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Processo  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String url = null;
  private String name = null;
  private String businessKey = null;
  private Boolean suspended = null;
  private Boolean ended = null;
  private String processDefinitionId = null;
  private String processDefinitionUrl = null;
  private String processDefinitionName = null;
  private String processDefinitionDescription = null;
  private String activityId = null;
  private String startUserId = null;
  private String startTime = null;
  private String tenantId = null;
  private Boolean completed = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: url 
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  


  // nome originario nello yaml: name 
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  


  // nome originario nello yaml: businessKey 
  public String getBusinessKey() {
    return businessKey;
  }
  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }

  /**
   **/
  


  // nome originario nello yaml: suspended 
  public Boolean isSuspended() {
    return suspended;
  }
  public void setSuspended(Boolean suspended) {
    this.suspended = suspended;
  }

  /**
   **/
  


  // nome originario nello yaml: ended 
  public Boolean isEnded() {
    return ended;
  }
  public void setEnded(Boolean ended) {
    this.ended = ended;
  }

  /**
   **/
  


  // nome originario nello yaml: processDefinitionId 
  public String getProcessDefinitionId() {
    return processDefinitionId;
  }
  public void setProcessDefinitionId(String processDefinitionId) {
    this.processDefinitionId = processDefinitionId;
  }

  /**
   **/
  


  // nome originario nello yaml: processDefinitionUrl 
  public String getProcessDefinitionUrl() {
    return processDefinitionUrl;
  }
  public void setProcessDefinitionUrl(String processDefinitionUrl) {
    this.processDefinitionUrl = processDefinitionUrl;
  }

  /**
   **/
  


  // nome originario nello yaml: processDefinitionName 
  public String getProcessDefinitionName() {
    return processDefinitionName;
  }
  public void setProcessDefinitionName(String processDefinitionName) {
    this.processDefinitionName = processDefinitionName;
  }

  /**
   **/
  


  // nome originario nello yaml: processDefinitionDescription 
  public String getProcessDefinitionDescription() {
    return processDefinitionDescription;
  }
  public void setProcessDefinitionDescription(String processDefinitionDescription) {
    this.processDefinitionDescription = processDefinitionDescription;
  }

  /**
   **/
  


  // nome originario nello yaml: activityId 
  public String getActivityId() {
    return activityId;
  }
  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }

  /**
   **/
  


  // nome originario nello yaml: startUserId 
  public String getStartUserId() {
    return startUserId;
  }
  public void setStartUserId(String startUserId) {
    this.startUserId = startUserId;
  }

  /**
   **/
  


  // nome originario nello yaml: startTime 
  public String getStartTime() {
    return startTime;
  }
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  /**
   **/
  


  // nome originario nello yaml: tenantId 
  public String getTenantId() {
    return tenantId;
  }
  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  /**
   **/
  


  // nome originario nello yaml: completed 
  public Boolean isCompleted() {
    return completed;
  }
  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Processo processo = (Processo) o;
    return Objects.equals(id, processo.id) &&
        Objects.equals(url, processo.url) &&
        Objects.equals(name, processo.name) &&
        Objects.equals(businessKey, processo.businessKey) &&
        Objects.equals(suspended, processo.suspended) &&
        Objects.equals(ended, processo.ended) &&
        Objects.equals(processDefinitionId, processo.processDefinitionId) &&
        Objects.equals(processDefinitionUrl, processo.processDefinitionUrl) &&
        Objects.equals(processDefinitionName, processo.processDefinitionName) &&
        Objects.equals(processDefinitionDescription, processo.processDefinitionDescription) &&
        Objects.equals(activityId, processo.activityId) &&
        Objects.equals(startUserId, processo.startUserId) &&
        Objects.equals(startTime, processo.startTime) &&
        Objects.equals(tenantId, processo.tenantId) &&
        Objects.equals(completed, processo.completed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, url, name, businessKey, suspended, ended, processDefinitionId, processDefinitionUrl, processDefinitionName, processDefinitionDescription, activityId, startUserId, startTime, tenantId, completed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Processo {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    businessKey: ").append(toIndentedString(businessKey)).append("\n");
    sb.append("    suspended: ").append(toIndentedString(suspended)).append("\n");
    sb.append("    ended: ").append(toIndentedString(ended)).append("\n");
    sb.append("    processDefinitionId: ").append(toIndentedString(processDefinitionId)).append("\n");
    sb.append("    processDefinitionUrl: ").append(toIndentedString(processDefinitionUrl)).append("\n");
    sb.append("    processDefinitionName: ").append(toIndentedString(processDefinitionName)).append("\n");
    sb.append("    processDefinitionDescription: ").append(toIndentedString(processDefinitionDescription)).append("\n");
    sb.append("    activityId: ").append(toIndentedString(activityId)).append("\n");
    sb.append("    startUserId: ").append(toIndentedString(startUserId)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    completed: ").append(toIndentedString(completed)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

