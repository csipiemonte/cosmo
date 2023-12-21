/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import java.math.BigDecimal;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Task  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String action = null;
  private String assignee = null;
  private String createTime = null;
  private String delegationState = null;
  private String description = null;
  private String dueDate = null;
  private String execution = null;
  private String name = null;
  private String category = null;
  private String owner = null;
  private String parentTaskId = null;
  private BigDecimal priority = null;
  private String processDefinition = null;
  private String processInstance = null;
  private Boolean suspended = null;
  private String taskDefinitionKey = null;
  private String url = null;
  private String tenantId = null;
  private String formKey = null;
  private String processInstanceId = null;
  private List<Object> variables = new ArrayList<>();
  private String cancellationDate = null;
  private List<Task> subtasks = new ArrayList<>();

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
  


  // nome originario nello yaml: action 
  public String getAction() {
    return action;
  }
  public void setAction(String action) {
    this.action = action;
  }

  /**
   **/
  


  // nome originario nello yaml: assignee 
  public String getAssignee() {
    return assignee;
  }
  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }

  /**
   **/
  


  // nome originario nello yaml: createTime 
  public String getCreateTime() {
    return createTime;
  }
  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  /**
   **/
  


  // nome originario nello yaml: delegationState 
  public String getDelegationState() {
    return delegationState;
  }
  public void setDelegationState(String delegationState) {
    this.delegationState = delegationState;
  }

  /**
   **/
  


  // nome originario nello yaml: description 
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   **/
  


  // nome originario nello yaml: dueDate 
  public String getDueDate() {
    return dueDate;
  }
  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }

  /**
   **/
  


  // nome originario nello yaml: execution 
  public String getExecution() {
    return execution;
  }
  public void setExecution(String execution) {
    this.execution = execution;
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
  


  // nome originario nello yaml: category 
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   **/
  


  // nome originario nello yaml: owner 
  public String getOwner() {
    return owner;
  }
  public void setOwner(String owner) {
    this.owner = owner;
  }

  /**
   **/
  


  // nome originario nello yaml: parentTaskId 
  public String getParentTaskId() {
    return parentTaskId;
  }
  public void setParentTaskId(String parentTaskId) {
    this.parentTaskId = parentTaskId;
  }

  /**
   **/
  


  // nome originario nello yaml: priority 
  public BigDecimal getPriority() {
    return priority;
  }
  public void setPriority(BigDecimal priority) {
    this.priority = priority;
  }

  /**
   **/
  


  // nome originario nello yaml: processDefinition 
  public String getProcessDefinition() {
    return processDefinition;
  }
  public void setProcessDefinition(String processDefinition) {
    this.processDefinition = processDefinition;
  }

  /**
   **/
  


  // nome originario nello yaml: processInstance 
  public String getProcessInstance() {
    return processInstance;
  }
  public void setProcessInstance(String processInstance) {
    this.processInstance = processInstance;
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
  


  // nome originario nello yaml: taskDefinitionKey 
  public String getTaskDefinitionKey() {
    return taskDefinitionKey;
  }
  public void setTaskDefinitionKey(String taskDefinitionKey) {
    this.taskDefinitionKey = taskDefinitionKey;
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
  


  // nome originario nello yaml: tenantId 
  public String getTenantId() {
    return tenantId;
  }
  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  /**
   **/
  


  // nome originario nello yaml: formKey 
  public String getFormKey() {
    return formKey;
  }
  public void setFormKey(String formKey) {
    this.formKey = formKey;
  }

  /**
   **/
  


  // nome originario nello yaml: processInstanceId 
  public String getProcessInstanceId() {
    return processInstanceId;
  }
  public void setProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }

  /**
   **/
  


  // nome originario nello yaml: variables 
  public List<Object> getVariables() {
    return variables;
  }
  public void setVariables(List<Object> variables) {
    this.variables = variables;
  }

  /**
   **/
  


  // nome originario nello yaml: cancellationDate 
  public String getCancellationDate() {
    return cancellationDate;
  }
  public void setCancellationDate(String cancellationDate) {
    this.cancellationDate = cancellationDate;
  }

  /**
   **/
  


  // nome originario nello yaml: subtasks 
  public List<Task> getSubtasks() {
    return subtasks;
  }
  public void setSubtasks(List<Task> subtasks) {
    this.subtasks = subtasks;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Task task = (Task) o;
    return Objects.equals(id, task.id) &&
        Objects.equals(action, task.action) &&
        Objects.equals(assignee, task.assignee) &&
        Objects.equals(createTime, task.createTime) &&
        Objects.equals(delegationState, task.delegationState) &&
        Objects.equals(description, task.description) &&
        Objects.equals(dueDate, task.dueDate) &&
        Objects.equals(execution, task.execution) &&
        Objects.equals(name, task.name) &&
        Objects.equals(category, task.category) &&
        Objects.equals(owner, task.owner) &&
        Objects.equals(parentTaskId, task.parentTaskId) &&
        Objects.equals(priority, task.priority) &&
        Objects.equals(processDefinition, task.processDefinition) &&
        Objects.equals(processInstance, task.processInstance) &&
        Objects.equals(suspended, task.suspended) &&
        Objects.equals(taskDefinitionKey, task.taskDefinitionKey) &&
        Objects.equals(url, task.url) &&
        Objects.equals(tenantId, task.tenantId) &&
        Objects.equals(formKey, task.formKey) &&
        Objects.equals(processInstanceId, task.processInstanceId) &&
        Objects.equals(variables, task.variables) &&
        Objects.equals(cancellationDate, task.cancellationDate) &&
        Objects.equals(subtasks, task.subtasks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, action, assignee, createTime, delegationState, description, dueDate, execution, name, category, owner, parentTaskId, priority, processDefinition, processInstance, suspended, taskDefinitionKey, url, tenantId, formKey, processInstanceId, variables, cancellationDate, subtasks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Task {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    assignee: ").append(toIndentedString(assignee)).append("\n");
    sb.append("    createTime: ").append(toIndentedString(createTime)).append("\n");
    sb.append("    delegationState: ").append(toIndentedString(delegationState)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
    sb.append("    execution: ").append(toIndentedString(execution)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    parentTaskId: ").append(toIndentedString(parentTaskId)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    processDefinition: ").append(toIndentedString(processDefinition)).append("\n");
    sb.append("    processInstance: ").append(toIndentedString(processInstance)).append("\n");
    sb.append("    suspended: ").append(toIndentedString(suspended)).append("\n");
    sb.append("    taskDefinitionKey: ").append(toIndentedString(taskDefinitionKey)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    formKey: ").append(toIndentedString(formKey)).append("\n");
    sb.append("    processInstanceId: ").append(toIndentedString(processInstanceId)).append("\n");
    sb.append("    variables: ").append(toIndentedString(variables)).append("\n");
    sb.append("    cancellationDate: ").append(toIndentedString(cancellationDate)).append("\n");
    sb.append("    subtasks: ").append(toIndentedString(subtasks)).append("\n");
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

