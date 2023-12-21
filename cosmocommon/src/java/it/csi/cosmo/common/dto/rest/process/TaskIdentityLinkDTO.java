/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.rest.process;

/**
 *
 */

/**
 *
 *
 */
public class TaskIdentityLinkDTO {

  private String groupId;
  private String userId;
  private String type;

  public TaskIdentityLinkDTO() {
    // Auto-generated constructor stub
  }

  /**
   * @return the groupId
   */
  public String getGroupId() {
    return groupId;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the userId
   */
  public String getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "TaskIdentityLink [groupId=" + groupId + ", userId=" + userId + ", type=" + type + "]";
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((userId == null) ? 0 : userId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TaskIdentityLinkDTO other = (TaskIdentityLinkDTO) obj;
    if (groupId == null) {
      if (other.groupId != null) {
        return false;
      }
    } else if (!groupId.equals(other.groupId)) {
      return false;
    }

    if (type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!type.equals(other.type)) {
      return false;
    }

    if (userId == null) {
      if (other.userId != null) {
        return false;
      }
    } else if (!userId.equals(other.userId)) {
      return false;
    }

    return true;
  }
}
