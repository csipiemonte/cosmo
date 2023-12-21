/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.mail.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */

public class CosmoMail implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -626925741958383776L;

  private String subject;

  private String text;

  private String from;

  private String fromName;

  private List<String> to;

  private List<String> cc;

  private List<String> bcc;
  
  private List<CosmoMailAttachment> attachments;

  private CosmoMail(Builder builder) {
    this.subject = builder.subject;
    this.text = builder.text;
    this.from = builder.from;
    this.fromName = builder.fromName;
    this.to = new ArrayList<>(builder.to);
    this.cc = new ArrayList<>(builder.cc);
    this.bcc = new ArrayList<>(builder.bcc);
    this.attachments = new ArrayList<>(builder.attachments);
  }

  public CosmoMail() {
    to = new ArrayList<>();
    cc = new ArrayList<>();
    bcc = new ArrayList<>();
    attachments = new ArrayList<>();
  }

  public String getSubject() {
    return subject;
  }

  public String getText() {
    return text;
  }

  public String getFrom() {
    return from;
  }

  public String getFromName() {
    return fromName;
  }

  public List<String> getTo() {
    return to;
  }

  public List<String> getCc() {
    return cc;
  }

  public List<String> getBcc() {
    return bcc;
  }

  public List<CosmoMailAttachment> getAttachments() {
    return attachments;
  }

  /**
   * Creates builder to build {@link CosmoMail}.
   *
   * @return created builder
   */
  public static ISubjectStage builder() {
    return new Builder();
  }

  public interface ISubjectStage {
    public ITextStage withSubject(String subject);
  }

  public interface ITextStage {
    public IBuildStage withText(String text);
  }

  public interface IBuildStage {

    public IBuildStage withFrom(String from);

    public IBuildStage withFromName(String fromName);

    public IBuildStage withTo(List<String> to);

    public IBuildStage withCc(List<String> cc);

    public IBuildStage withBcc(List<String> bcc);

    public IBuildStage withAttachments(List<CosmoMailAttachment> attachments);

    public CosmoMail build();
  }

  /**
   * Builder to build {@link CosmoMail}.
   */
  public static final class Builder implements ISubjectStage, ITextStage, IBuildStage {
    private String subject;
    private String text;
    private String from;
    private String fromName;
    private List<String> to = Collections.emptyList();
    private List<String> cc = Collections.emptyList();
    private List<String> bcc = Collections.emptyList();
    private List<CosmoMailAttachment> attachments = Collections.emptyList();

    private Builder() {}

    @Override
    public IBuildStage withAttachments(List<CosmoMailAttachment> attachments) {
      this.attachments = attachments;
      return this;
    }
    
    @Override
    public ITextStage withSubject(String subject) {
      this.subject = subject;
      return this;
    }

    @Override
    public IBuildStage withText(String text) {
      this.text = text;
      return this;
    }

    @Override
    public IBuildStage withFrom(String from) {
      this.from = from;
      return this;
    }

    @Override
    public IBuildStage withFromName(String fromName) {
      this.fromName = fromName;
      return this;
    }

    @Override
    public IBuildStage withTo(List<String> to) {
      this.to = to;
      return this;
    }

    @Override
    public IBuildStage withCc(List<String> cc) {
      this.cc = cc;
      return this;
    }

    @Override
    public IBuildStage withBcc(List<String> bcc) {
      this.bcc = bcc;
      return this;
    }

    @Override
    public CosmoMail build() {
      return new CosmoMail(this);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((subject == null) ? 0 : subject.hashCode());
    result = prime * result + ((text == null) ? 0 : text.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) { // NOSONAR
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CosmoMail other = (CosmoMail) obj;
    if (bcc == null) {
      if (other.bcc != null) {
        return false;
      }
    } else if (!bcc.equals(other.bcc)) {
      return false;
    }
    if (cc == null) {
      if (other.cc != null) {
        return false;
      }
    } else if (!cc.equals(other.cc)) {
      return false;
    }
    if (from == null) {
      if (other.from != null) {
        return false;
      }
    } else if (!from.equals(other.from)) {
      return false;
    }
    if (fromName == null) {
      if (other.fromName != null) {
        return false;
      }
    } else if (!fromName.equals(other.fromName)) {
      return false;
    }
    if (subject == null) {
      if (other.subject != null) {
        return false;
      }
    } else if (!subject.equals(other.subject)) {
      return false;
    }
    if (text == null) {
      if (other.text != null) {
        return false;
      }
    } else if (!text.equals(other.text)) {
      return false;
    }
    if (to == null) {
      if (other.to != null) {
        return false;
      }
    } else if (!to.equals(other.to)) {
      return false;
    }
    return true;
  }

}
