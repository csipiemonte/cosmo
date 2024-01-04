/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.dto.dosign;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Signer implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<TimeStamp> archiveTimeStamps;
	private Certificate certificateId;
	private String certificateSn;
	private int certificateStatus;
	private int certificateStatusCode;
	private String certificateStatusDescription;
	private String certificateStatusInfo;
	private byte[] certificateValue;
	private int certificateVerificationMode;
	private int certificationLevel;
	private int complianceEnvironment;
	private String contact;
	private boolean corruptedData;
	private String crlIssueDn;
	private LocalDateTime crlNextUpdateTime;
	private LocalDateTime crlRevocationDate;
	private LocalDateTime crlUpdateTime;
	private String crlUrl;
	private String dateOfBirth;
	private int digestAlgorithm;
	private String digestAlgorithmName;
	private byte[] digestValue;
	private LocalDateTime endDate;
	private String fiscalCode;
	private int invalidSignCount;
	private String issuerDn;
	private int keyType;
	private int keyUsage;
	private String location;
	private byte[] md5Fingerprint;
	private LocalDateTime ocspProducedAt;
	private String ocspUrl;
	private List<CertificatePolicy> policies;
	private String qcCompliance;
	private int qcLimitValue;
	private String qcLimitValueCurrency;
	private int qcRetentionPeriod;
	private String qcSscd;
	private String reason;
	private List<XmlReference> references;
	private List<TimeStamp> refsOnlyTimeStamps;
	private int revision;
	private byte[] shaFingerprint;
	private List<TimeStamp> sigAndRefsTimeStamps;
	private int signatureAlgorithm;
	private String signatureAlgorithmName;
	private int signatureCompliance;
	private int signatureComplianceCode;
	private String signatureComplianceInfo;
	private int signatureFormat;
	private XmlSignatureProductionPlace signatureProductionPlace;
	private List<TimeStamp> signatureTimeStamps;
	private byte[] signatureValue;
	private int signerCount;
	private List<Signer> signers;
	private LocalDateTime signingTime;
	private LocalDateTime startDate;
	private String subjectDn;
	private List<TimeStamp> timeStamps;
	private LocalDateTime verificationDate;

	public List<TimeStamp> getArchiveTimeStamps() {
		return archiveTimeStamps;
	}

	public void setArchiveTimeStamps(List<TimeStamp> archiveTimeStamps) {
		this.archiveTimeStamps = archiveTimeStamps;
	}

	public Certificate getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(Certificate certificateId) {
		this.certificateId = certificateId;
	}

	public String getCertificateSn() {
		return certificateSn;
	}

	public void setCertificateSn(String certificateSn) {
		this.certificateSn = certificateSn;
	}

	public int getCertificateStatus() {
		return certificateStatus;
	}

	public void setCertificateStatus(int certificateStatus) {
		this.certificateStatus = certificateStatus;
	}

	public int getCertificateStatusCode() {
		return certificateStatusCode;
	}

	public void setCertificateStatusCode(int certificateStatusCode) {
		this.certificateStatusCode = certificateStatusCode;
	}

	public String getCertificateStatusDescription() {
		return certificateStatusDescription;
	}

	public void setCertificateStatusDescription(String certificateStatusDescription) {
		this.certificateStatusDescription = certificateStatusDescription;
	}

	public String getCertificateStatusInfo() {
		return certificateStatusInfo;
	}

	public void setCertificateStatusInfo(String certificateStatusInfo) {
		this.certificateStatusInfo = certificateStatusInfo;
	}

	public byte[] getCertificateValue() {
		return certificateValue;
	}

	public void setCertificateValue(byte[] certificateValue) {
		this.certificateValue = certificateValue;
	}

	public int getCertificateVerificationMode() {
		return certificateVerificationMode;
	}

	public void setCertificateVerificationMode(int certificateVerificationMode) {
		this.certificateVerificationMode = certificateVerificationMode;
	}

	public int getCertificationLevel() {
		return certificationLevel;
	}

	public void setCertificationLevel(int certificationLevel) {
		this.certificationLevel = certificationLevel;
	}

	public int getComplianceEnvironment() {
		return complianceEnvironment;
	}

	public void setComplianceEnvironment(int complianceEnvironment) {
		this.complianceEnvironment = complianceEnvironment;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public boolean isCorruptedData() {
		return corruptedData;
	}

	public void setCorruptedData(boolean corruptedData) {
		this.corruptedData = corruptedData;
	}

	public String getCrlIssueDn() {
		return crlIssueDn;
	}

	public void setCrlIssueDn(String crlIssueDn) {
		this.crlIssueDn = crlIssueDn;
	}

	public LocalDateTime getCrlNextUpdateTime() {
		return crlNextUpdateTime;
	}

	public void setCrlNextUpdateTime(LocalDateTime crlNextUpdateTime) {
		this.crlNextUpdateTime = crlNextUpdateTime;
	}

	public LocalDateTime getCrlRevocationDate() {
		return crlRevocationDate;
	}

	public void setCrlRevocationDate(LocalDateTime crlRevocationDate) {
		this.crlRevocationDate = crlRevocationDate;
	}

	public LocalDateTime getCrlUpdateTime() {
		return crlUpdateTime;
	}

	public void setCrlUpdateTime(LocalDateTime crlUpdateTime) {
		this.crlUpdateTime = crlUpdateTime;
	}

	public String getCrlUrl() {
		return crlUrl;
	}

	public void setCrlUrl(String crlUrl) {
		this.crlUrl = crlUrl;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getDigestAlgorithm() {
		return digestAlgorithm;
	}

	public void setDigestAlgorithm(int digestAlgorithm) {
		this.digestAlgorithm = digestAlgorithm;
	}

	public String getDigestAlgorithmName() {
		return digestAlgorithmName;
	}

	public void setDigestAlgorithmName(String digestAlgorithmName) {
		this.digestAlgorithmName = digestAlgorithmName;
	}

	public byte[] getDigestValue() {
		return digestValue;
	}

	public void setDigestValue(byte[] digestValue) {
		this.digestValue = digestValue;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

	public int getInvalidSignCount() {
		return invalidSignCount;
	}

	public void setInvalidSignCount(int invalidSignCount) {
		this.invalidSignCount = invalidSignCount;
	}

	public String getIssuerDn() {
		return issuerDn;
	}

	public void setIssuerDn(String issuerDn) {
		this.issuerDn = issuerDn;
	}

	public int getKeyType() {
		return keyType;
	}

	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}

	public int getKeyUsage() {
		return keyUsage;
	}

	public void setKeyUsage(int keyUsage) {
		this.keyUsage = keyUsage;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public byte[] getMd5Fingerprint() {
		return md5Fingerprint;
	}

	public void setMd5Fingerprint(byte[] md5Fingerprint) {
		this.md5Fingerprint = md5Fingerprint;
	}

	public LocalDateTime getOcspProducedAt() {
		return ocspProducedAt;
	}

	public void setOcspProducedAt(LocalDateTime ocspProducedAt) {
		this.ocspProducedAt = ocspProducedAt;
	}

	public String getOcspUrl() {
		return ocspUrl;
	}

	public void setOcspUrl(String ocspUrl) {
		this.ocspUrl = ocspUrl;
	}

	public List<CertificatePolicy> getPolicies() {
		return policies;
	}

	public void setPolicies(List<CertificatePolicy> policies) {
		this.policies = policies;
	}

	public String getQcCompliance() {
		return qcCompliance;
	}

	public void setQcCompliance(String qcCompliance) {
		this.qcCompliance = qcCompliance;
	}

	public int getQcLimitValue() {
		return qcLimitValue;
	}

	public void setQcLimitValue(int qcLimitValue) {
		this.qcLimitValue = qcLimitValue;
	}

	public String getQcLimitValueCurrency() {
		return qcLimitValueCurrency;
	}

	public void setQcLimitValueCurrency(String qcLimitValueCurrency) {
		this.qcLimitValueCurrency = qcLimitValueCurrency;
	}

	public int getQcRetentionPeriod() {
		return qcRetentionPeriod;
	}

	public void setQcRetentionPeriod(int qcRetentionPeriod) {
		this.qcRetentionPeriod = qcRetentionPeriod;
	}

	public String getQcSscd() {
		return qcSscd;
	}

	public void setQcSscd(String qcSscd) {
		this.qcSscd = qcSscd;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<XmlReference> getReferences() {
		return references;
	}

	public void setReferences(List<XmlReference> references) {
		this.references = references;
	}

	public List<TimeStamp> getRefsOnlyTimeStamps() {
		return refsOnlyTimeStamps;
	}

	public void setRefsOnlyTimeStamps(List<TimeStamp> refsOnlyTimeStamps) {
		this.refsOnlyTimeStamps = refsOnlyTimeStamps;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public byte[] getShaFingerprint() {
		return shaFingerprint;
	}

	public void setShaFingerprint(byte[] shaFingerprint) {
		this.shaFingerprint = shaFingerprint;
	}

	public List<TimeStamp> getSigAndRefsTimeStamps() {
		return sigAndRefsTimeStamps;
	}

	public void setSigAndRefsTimeStamps(List<TimeStamp> sigAndRefsTimeStamps) {
		this.sigAndRefsTimeStamps = sigAndRefsTimeStamps;
	}

	public int getSignatureAlgorithm() {
		return signatureAlgorithm;
	}

	public void setSignatureAlgorithm(int signatureAlgorithm) {
		this.signatureAlgorithm = signatureAlgorithm;
	}

	public String getSignatureAlgorithmName() {
		return signatureAlgorithmName;
	}

	public void setSignatureAlgorithmName(String signatureAlgorithmName) {
		this.signatureAlgorithmName = signatureAlgorithmName;
	}

	public int getSignatureCompliance() {
		return signatureCompliance;
	}

	public void setSignatureCompliance(int signatureCompliance) {
		this.signatureCompliance = signatureCompliance;
	}

	public int getSignatureComplianceCode() {
		return signatureComplianceCode;
	}

	public void setSignatureComplianceCode(int signatureComplianceCode) {
		this.signatureComplianceCode = signatureComplianceCode;
	}

	public String getSignatureComplianceInfo() {
		return signatureComplianceInfo;
	}

	public void setSignatureComplianceInfo(String signatureComplianceInfo) {
		this.signatureComplianceInfo = signatureComplianceInfo;
	}

	public int getSignatureFormat() {
		return signatureFormat;
	}

	public void setSignatureFormat(int signatureFormat) {
		this.signatureFormat = signatureFormat;
	}

	public XmlSignatureProductionPlace getSignatureProductionPlace() {
		return signatureProductionPlace;
	}

	public void setSignatureProductionPlace(XmlSignatureProductionPlace signatureProductionPlace) {
		this.signatureProductionPlace = signatureProductionPlace;
	}

	public List<TimeStamp> getSignatureTimeStamps() {
		return signatureTimeStamps;
	}

	public void setSignatureTimeStamps(List<TimeStamp> signatureTimeStamps) {
		this.signatureTimeStamps = signatureTimeStamps;
	}

	public byte[] getSignatureValue() {
		return signatureValue;
	}

	public void setSignatureValue(byte[] signatureValue) {
		this.signatureValue = signatureValue;
	}

	public int getSignerCount() {
		return signerCount;
	}

	public void setSignerCount(int signerCount) {
		this.signerCount = signerCount;
	}

	public List<Signer> getSigners() {
		return signers;
	}

	public void setSigners(List<Signer> signers) {
		this.signers = signers;
	}

	public LocalDateTime getSigningTime() {
		return signingTime;
	}

	public void setSigningTime(LocalDateTime signingTime) {
		this.signingTime = signingTime;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public String getSubjectDn() {
		return subjectDn;
	}

	public void setSubjectDn(String subjectDn) {
		this.subjectDn = subjectDn;
	}

	public List<TimeStamp> getTimeStamps() {
		return timeStamps;
	}

	public void setTimeStamps(List<TimeStamp> timeStamps) {
		this.timeStamps = timeStamps;
	}

	public LocalDateTime getVerificationDate() {
		return verificationDate;
	}

	public void setVerificationDate(LocalDateTime verificationDate) {
		this.verificationDate = verificationDate;
	}
}
