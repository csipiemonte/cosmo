/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.async;

import java.io.Serializable;
import java.util.function.Function;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.logger.CosmoLogger;

public class DefaultLongTaskStepExecutor implements LongTaskStepExecutor {

  private final CosmoLogger log;

  private DefaultLongTaskStepExecutor(Builder builder) {
    this.log = builder.log;
  }

  @Override
  public <T extends Serializable> T apply(Function<LongTask<T>, T> executor, LongTask<T> step) {
    if (step != null) {
      log.debug("apply", "running task step {}", step.getName());
    }
    return executor.apply(step);
  }

  /**
   * Creates builder to build {@link DefaultLongTaskStepExecutor}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link DefaultLongTaskStepExecutor}.
   */
  public static final class Builder {
    private CosmoLogger log;

    private Builder() {}

    public Builder withLogger(CosmoLogger logger) {
      this.log = logger;
      return this;
    }

    public DefaultLongTaskStepExecutor build() {
      return new DefaultLongTaskStepExecutor(this);
    }
  }

}
