/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
export interface NotificationPayload {
    type: string;
    message: string;
    title?: string;
    options?: any;
    url?: string;
    urlDescription?: string;
    notificationId?: number;
}
