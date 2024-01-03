/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.csi.cosmo.common.entities.CsiLogAudit;



public interface CsiLogAuditRepository extends JpaRepository<CsiLogAudit, Long> {

}
