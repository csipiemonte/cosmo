/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.rest.process;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class TaskInstanceDTO {

  private String name;
  private String description;
  private String id;
  private String parentTaskId;
  private String assignee;
  private String formKey;
  private Set<TaskIdentityLinkDTO> identityLinks;

  public TaskInstanceDTO() {
    // Auto-generated constructor stub
    identityLinks = new HashSet<>();
  }

  public String getFormKey() {
    return formKey;
  }

  public void setFormKey(String formKey) {
    this.formKey = formKey;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
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

  /**
   * @return the parentTaskId
   */
  public String getParentTaskId() {
    return parentTaskId;
  }

  /**
   * @param parentTaskId the parentTaskId to set
   */
  public void setParentTaskId(String parentTaskId) {
    this.parentTaskId = parentTaskId;
  }

  @Override
  public String toString() {
    return "TaskInstance [name=" + name + ", description=" + description + ", id=" + id
        + ", parentTaskId=" + parentTaskId + ", assignee=" + assignee + "]";
  }

  /**
   * @return the assignee
   */
  public String getAssignee() {
    return assignee;
  }

  /**
   * @param assignee the assignee to set
   */
  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }

  /**
   * @return the identityLinks
   */
  public Set<TaskIdentityLinkDTO> getIdentityLinks() {
    return identityLinks;
  }

  /**
   * @param identityLinks the identityLinks to set
   */
  public void setIdentityLinks(Set<TaskIdentityLinkDTO> identityLinks) {
    this.identityLinks = identityLinks;
  }

}
