/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.model;

import java.io.InputStream;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

/**
 *
 */

public class RetrievedUploadSession implements RetrievedContent {

  private InputStream contentStream;

  private Path workingFolder;

  private Path sessionFolder;

  private Path partsFolder;

  private Path completedMarkerFile;

  private UploadSessionMetadata session;

  private List<RetrievedUploadSessionPart> parts;

  private RetrievedUploadSession(Builder builder) {
    this.contentStream = builder.contentStream;
    this.workingFolder = builder.workingFolder;
    this.sessionFolder = builder.sessionFolder;
    this.partsFolder = builder.partsFolder;
    this.completedMarkerFile = builder.completedMarkerFile;
    this.session = builder.session;
    this.parts = builder.parts;
  }

  public RetrievedUploadSession() {}

  @Override
  public String getUploadUUID() {
    if (session == null) {
      return null;
    }
    return session.getSessionUUID();
  }

  @Override
  public String getFilename() {
    if (session == null) {
      return null;
    }
    return session.getFileName();
  }

  @Override
  public String getContentType() {
    if (session == null) {
      return null;
    }
    return session.getMimeType();
  }

  @Override
  public Long getContentSize() {
    if (session == null) {
      return null;
    }
    return session.getSize();
  }

  @Override
  public ZonedDateTime getUploadedAt() {
    if (session == null) {
      return null;
    }
    return session.getUploadedAt();
  }

  @Override
  public InputStream getContentStream() {
    return contentStream;
  }

  public void setContentStream(InputStream contentStream) {
    this.contentStream = contentStream;
  }

  public List<RetrievedUploadSessionPart> getParts() {
    return parts;
  }

  public void setParts(List<RetrievedUploadSessionPart> parts) {
    this.parts = parts;
  }

  public Path getCompletedMarkerFile() {
    return completedMarkerFile;
  }

  public void setCompletedMarkerFile(Path completedMarkerFile) {
    this.completedMarkerFile = completedMarkerFile;
  }

  public Path getSessionFolder() {
    return sessionFolder;
  }

  public void setSessionFolder(Path sessionFolder) {
    this.sessionFolder = sessionFolder;
  }

  public Path getPartsFolder() {
    return partsFolder;
  }

  public void setPartsFolder(Path partsFolder) {
    this.partsFolder = partsFolder;
  }

  @Override
  public Path getWorkingFolder() {
    return workingFolder;
  }

  public void setWorkingFolder(Path workingFolder) {
    this.workingFolder = workingFolder;
  }

  public UploadSessionMetadata getSession() {
    return session;
  }

  public void setSession(UploadSessionMetadata session) {
    this.session = session;
  }

  /**
   * Creates builder to build {@link RetrievedUploadSession}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link RetrievedUploadSession}.
   */
  public static final class Builder {
    private InputStream contentStream;
    private Path workingFolder;
    private Path sessionFolder;
    private Path partsFolder;
    private Path completedMarkerFile;
    private UploadSessionMetadata session;
    private List<RetrievedUploadSessionPart> parts = Collections.emptyList();

    private Builder() {}

    public Builder withContentStream(InputStream contentStream) {
      this.contentStream = contentStream;
      return this;
    }

    public Builder withWorkingFolder(Path workingFolder) {
      this.workingFolder = workingFolder;
      return this;
    }

    public Builder withSessionFolder(Path sessionFolder) {
      this.sessionFolder = sessionFolder;
      return this;
    }

    public Builder withPartsFolder(Path partsFolder) {
      this.partsFolder = partsFolder;
      return this;
    }

    public Builder withCompletedMarkerFile(Path completedMarkerFile) {
      this.completedMarkerFile = completedMarkerFile;
      return this;
    }

    public Builder withSession(UploadSessionMetadata session) {
      this.session = session;
      return this;
    }

    public Builder withParts(List<RetrievedUploadSessionPart> parts) {
      this.parts = parts;
      return this;
    }

    public RetrievedUploadSession build() {
      return new RetrievedUploadSession(this);
    }
  }


}
