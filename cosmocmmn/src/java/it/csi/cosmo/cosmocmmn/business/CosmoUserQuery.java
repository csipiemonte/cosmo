/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business;

import java.util.HashMap;
import java.util.Map;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.idm.engine.impl.UserQueryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CosmoUserQuery extends UserQueryImpl {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(CosmoUserQuery.class);

  static Map<String, String> operators = new HashMap<>();
  static Map<String, String> fields = new HashMap<>();

  static String[] methods = {"getEmail", "getEmailLike", "getId", "getIdIgnoreCase", "getFirstName",
      "getFirstNameLike", "getFirstNameLikeIgnoreCase", "getFullNameLike",
      "getFullNameLikeIgnoreCase", "getGroupId", "getGroupIds", "getIds", "getLastName",
      "getLastNameLike", "getLastNameLikeIgnoreCase"};

  static {
    operators.put("getEmail", "eq");
    operators.put("getEmailLike", "c");
    operators.put("getId", "eq");
    operators.put("getIdIgnoreCase", "ci");
    operators.put("getFirstName", "eq");
    operators.put("getFirstNameLike", "c");
    operators.put("getFirstNameLikeIgnoreCase", "ci");
    operators.put("getFullNameLike", "c");
    operators.put("getFullNameLikeIgnoreCase", "ci");
    operators.put("getGroupId", "eq");
    operators.put("getGroupIds", "in");
    operators.put("getIds", "in");
    operators.put("getLastName", "eq");
    operators.put("getLastNameLike", "c");
    operators.put("getLastNameLikeIgnoreCase", "ci");
    fields.put("getEmail", "email");
    fields.put("getEmailLike", "email");
    fields.put("getId", "id");
    fields.put("getIdIgnoreCase", "id");
    fields.put("getFirstName", "firstName");
    fields.put("getFirstNameLike", "firstName");
    fields.put("getFirstNameLikeIgnoreCase", "firstName");
    fields.put("getFullNameLike", "fullName");
    fields.put("getFullNameLikeIgnoreCase", "fullName");
    fields.put("getGroupId", "groupId");
    fields.put("getGroupIds", "groupId");
    fields.put("getIds", "id");
    fields.put("getLastName", "lastName");
    fields.put("getLastNameLike", "lastName");
    fields.put("getLastNameLikeIgnoreCase", "lastName");
  }

  @Override
  public long executeCount(CommandContext commandContext) {
    return executeList(commandContext).size();
  }



}
