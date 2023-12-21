/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business;

import java.util.ArrayList;
import java.util.List;
import javax.naming.directory.SearchControls;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.idm.api.Group;
import org.flowable.idm.engine.impl.GroupQueryImpl;

public class CosmoGroupQuery extends GroupQueryImpl {

  private static final long serialVersionUID = 1L;

  @Override
  public long executeCount(CommandContext commandContext) {
    return executeQuery().size();
  }

  @Override
  public List<Group> executeList(CommandContext commandContext) {
    return executeQuery();
  }

  protected List<Group> executeQuery() {
    if (getUserId() != null) {
      return findGroupsByUser(getUserId());
    } else if (getId() != null) {
      return findGroupsById(getId());
    } else {
      return findAllGroups();
    }
  }

  protected List<Group> findGroupsByUser(String userId) {

    return executeGroupQuery(userId);
  }

  protected List<Group> findGroupsById(String id) {
    String searchExpression = "";
    return executeGroupQuery(searchExpression);
  }

  protected List<Group> findAllGroups() {
    String searchExpression = "";
    return executeGroupQuery(searchExpression);
  }

  protected List<Group> executeGroupQuery(final String searchExpression) {
    return new ArrayList<>();
  }

  protected SearchControls createSearchControls() {
    SearchControls searchControls = new SearchControls();
    searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
    return searchControls;
  }
}
