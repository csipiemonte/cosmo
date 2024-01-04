/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.soap.index2.internal;

import java.util.LinkedList;
import java.util.List;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexEntity;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexFolder;


/**
 *
 */
public class IndexTransactionalOperationContext {

  // LinkedList as type because has to be ordered
  private LinkedList<Operation> operations;

  private boolean status;

  public boolean isStatus () {
    return status;
  }

  public void setStatus ( boolean status ) {
    this.status = status;
  }

  public static IndexTransactionalOperationContext create () {
    return IndexTransactionalOperationContext.builder ().build ();
  }

  @Override
  public String toString () {
    final int maxLen = 3;
    StringBuilder builder2 = new StringBuilder ();
    builder2.append ( "IndexTransactionalOperationContext [operations=" );
    builder2.append ( operations != null ? operations.subList ( 0, Math.min ( operations.size (), maxLen ) ) : null );
    builder2.append ( "]" );
    return builder2.toString ();
  }

  private IndexTransactionalOperationContext() {
    operations = new LinkedList<> ();
    status = false;
  }

  public void clear () {
    operations.clear ();
  }

  public List<Operation> getOperations () {
    return operations;
  }

  public List<Operation> getOperationsInRollbackOrder () {
    LinkedList<Operation> output = new LinkedList<> ();
    for ( Operation o: operations ) {
      output.addFirst ( o );
    }
    return output;
  }

  public void registerDelete ( String uuid ) {
    operations.add ( Operation.builder ()
        .withType ( Operation.OperationType.DELETE )
        .withUuid ( uuid )
        .build () );
  }

  public void registerDelete ( IndexEntity entity ) {
    operations.add ( Operation.builder ()
        .withType ( Operation.OperationType.DELETE )
        .withUuid ( entity.getUid () )
        .withEntityBefore ( entity )
        .build () );
  }

  public void registerDelete ( IndexFolder entity ) {
    operations.add ( Operation.builder ()
        .withType ( Operation.OperationType.DELETE )
        .withUuid ( entity.getUid () )
        .withFolderBefore ( entity )
        .build () );
  }

  public void registerCreate ( String uuid ) {
    operations.add ( Operation.builder ()
        .withType ( Operation.OperationType.CREATE )
        .withUuid ( uuid )
        .build () );
  }

  public void registerCreate ( IndexEntity entity ) {
    operations.add ( Operation.builder ()
        .withType ( Operation.OperationType.CREATE )
        .withUuid ( entity.getUid () )
        .withEntityAfter ( entity )
        .build () );
  }

  public void registerCreate ( IndexFolder entity ) {
    operations.add ( Operation.builder ()
        .withType ( Operation.OperationType.CREATE )
        .withUuid ( entity.getUid () )
        .withFolderAfter ( entity )
        .build () );
  }

  public void registerUpdate ( IndexEntity entityBefore, IndexEntity entityAfter ) {
    operations.add ( Operation.builder ()
        .withType ( Operation.OperationType.UPDATE )
        .withUuid ( entityAfter.getUid () )
        .withEntityBefore ( entityBefore )
        .withEntityAfter ( entityAfter )
        .build () );
  }

  public void registerUpdate ( IndexFolder entityBefore, IndexFolder entityAfter ) {
    operations.add ( Operation.builder ()
        .withType ( Operation.OperationType.UPDATE )
        .withUuid ( entityAfter.getUid () )
        .withFolderBefore ( entityBefore )
        .withFolderAfter ( entityAfter )
        .build () );
  }

  public static class Operation {

    private OperationType type;

    private String uuid;

    private IndexEntity entityBefore;

    private IndexEntity entityAfter;

    private IndexFolder folderBefore;

    private IndexFolder folderAfter;


    private Operation ( Builder builder ) {
      type = builder.type;
      uuid = builder.uuid;
      entityBefore = builder.entityBefore;
      entityAfter = builder.entityAfter;
      folderBefore = builder.folderBefore;
      folderAfter = builder.folderAfter;
    }

    @Override
    public String toString () {
      StringBuilder builder2 = new StringBuilder ();
      builder2.append ( "Operation [type=" );
      builder2.append ( type );
      builder2.append ( ", uuid=" );
      builder2.append ( uuid );
      builder2.append ( ", entityBefore=" );
      builder2.append ( entityBefore );
      builder2.append ( ", entityAfter=" );
      builder2.append ( entityAfter );
      builder2.append ( ", folderBefore=" );
      builder2.append ( folderBefore );
      builder2.append ( ", folderAfter=" );
      builder2.append ( folderAfter );
      builder2.append ( "]" );
      return builder2.toString ();
    }

    public OperationType getType () {
      return type;
    }

    public String getUuid () {
      return uuid;
    }

    public IndexEntity getEntityBefore () {
      return entityBefore;
    }

    public IndexEntity getEntityAfter () {
      return entityAfter;
    }

    public IndexFolder getFolderBefore () {
      return folderBefore;
    }

    public IndexFolder getFolderAfter () {
      return folderAfter;
    }

    /**
     * Creates builder to build {@link Operation}.
     *
     * @return created builder
     */

    public static Builder builder () {
      return new Builder ();
    }

    /**
     * Builder to build {@link Operation}.
     */

    public static final class Builder {

      private OperationType type;

      private String uuid;

      private IndexEntity entityBefore;

      private IndexEntity entityAfter;

      private IndexFolder folderBefore;

      private IndexFolder folderAfter;

      private Builder () {
      }

      public Builder withType ( OperationType type ) {
        this.type = type;
        return this;
      }

      public Builder withUuid ( String uuid ) {
        this.uuid = uuid;
        return this;
      }

      public Builder withEntityBefore ( IndexEntity entityBefore ) {
        this.entityBefore = entityBefore;
        return this;
      }

      public Builder withEntityAfter ( IndexEntity entityAfter ) {
        this.entityAfter = entityAfter;
        return this;
      }

      public Builder withFolderBefore ( IndexFolder folderBefore ) {
        this.folderBefore = folderBefore;
        return this;
      }

      public Builder withFolderAfter ( IndexFolder folderAfter ) {
        this.folderAfter = folderAfter;
        return this;
      }

      public Operation build () {
        return new Operation ( this );
      }
    }

    public enum OperationType {
      CREATE,
      UPDATE,
      DELETE
    }

  }

  /**
   * Creates builder to build {@link IndexTransactionalOperationContext}.
   *
   * @return created builder
   */
  public static Builder builder () {
    return new Builder ();
  }

  /**
   * Builder to build {@link IndexTransactionalOperationContext}.
   */
  public static final class Builder {

    private Builder () {
    }

    public IndexTransactionalOperationContext build () {
      return new IndexTransactionalOperationContext();
    }
  }
}
