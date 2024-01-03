/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Commento } from '../models/api/cosmobusiness/commento';
import { PaginaCommenti } from '../models/api/cosmobusiness/paginaCommenti';
import { PaginaTask } from '../models/api/cosmobusiness/paginaTask';
import { Task } from '../models/api/cosmobusiness/task';
import { ApiUrls } from '../utilities/apiurls';

@Injectable()
export class TaskService {

  constructor(private http: HttpClient) { }

  public getCommentiTask(taskId: string | number): Observable<Commento[]> {
    if (!taskId) {
      throw new Error('TaskId is required');
    }

    return this.http.get<PaginaCommenti>(
      ApiUrls.GET_TASK_COMMENTI.replace('{idTask}', taskId + '')).pipe(
        map(pagina => pagina.elementi ?? []),
        map(list => list.sort((e1: Commento, e2: Commento) =>
          (e1.timestamp ?? '').localeCompare((e2.timestamp ?? '')))),
      );
  }

  public getSubtasks(taskId: string | number): Observable<Task[]> {
    if (!taskId) {
      throw new Error('TaskId is required');
    }

    return this.http.get<PaginaTask>(
      ApiUrls.GET_TASK_SUBTASKS.replace('{idTask}', taskId + '')).pipe(
        map(pagina => pagina.elementi ?? []),
        map(list => list.sort((e1: Task, e2: Task) =>
          (e1.createTime ?? '').localeCompare((e2.createTime ?? '')))),
      );
  }

  public creaSubtask(
    task: Task
  ): Observable<Task> {
    if (!task.parentTaskId) {
      throw new Error('ParentTaskId is required');
    }
    if (!task.assignee) {
      throw new Error('Assignee is required');
    }

    const payload: Task = {
      description: 'Richiesta di collaborazione sul task ' + task.parentTaskId,
      formKey: 'subtask',
      category: 'subtask',
      ...task
    };

    return this.http.post<Task>(ApiUrls.POST_TASK_SUBTASK, payload);
  }

  public creaCommentoTask(taskId: string | number, commento: string, reply?: boolean): Observable<Commento> {
    if (!taskId) {
      throw new Error('TaskId is required');
    }
    if (!commento) {
      throw new Error('Commento is required');
    }

    const payload: Commento = {
      messaggio: commento
    };

    const options = reply ? { params: new HttpParams().set('reply', reply.toString()) } : {};
    return this.http.post<Commento>(ApiUrls.POST_TASK_COMMENTI.replace('{idTask}', taskId + ''), payload, options);
  }

  /**
   * @deprecated sostituire con appoisto endpoint di completamento attivita'
   */
  public chiudiTask(task: Task): Observable<any> {
    if (!task?.id) {
      throw new Error('Task.Id is required');
    }

    const url = ApiUrls.POST_TASK.replace('{idTask}', task.id + '');

    const payload: Partial<Task> = {
      action: 'complete',
      assignee: task.assignee
    };

    return this.http.post<any>(url, payload);
  }

}
