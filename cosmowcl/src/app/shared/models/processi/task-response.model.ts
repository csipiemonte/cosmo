/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Task } from '../api/cosmobusiness/task';

/**
 * @deprecated utilizzare le classi generate sotto src/shared/models/api
 */
export interface TaskResponse {
    data: Task[];
    total: number;
    start: number;
    sort: string;
    order: string;
    size: number;

}
