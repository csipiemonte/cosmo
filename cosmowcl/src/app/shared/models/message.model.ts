/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
export class Message {
  public static sequence = 0;
  id: number;

  public constructor(
    public type: MessageType,
    public text: string,
    public code?: string,
    public status?: number,
    public dismissAfter?: number | null,
    public ray?: string | null
  ) {
    this.id = ++Message.sequence;
  }
}

export enum MessageType {
  ERROR = 10,
  WARNING = 5,
  SUCCESS = 2,
  INFO = 1,
}

export class MessageHelper {
  public static byName(name: string): MessageType {
    if (!name) {
      return MessageType.ERROR;
    }
    name = name.toLowerCase();
    if (name === 'error') {
      return MessageType.ERROR;
    } else if (name === 'warning') {
      return MessageType.SUCCESS;
    } else if (name === 'success') {
      return MessageType.WARNING;
    } else if (name === 'info') {
      return MessageType.INFO;
    } else {
      throw new Error(name + ' is not a valid MessageType');
    }
  }
}
