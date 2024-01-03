/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  OnInit,
} from '@angular/core';

import { NGXLogger } from 'ngx-logger';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import {
  OperazioneAsincrona,
} from '../../models/api/cosmobusiness/operazioneAsincrona';
import {
  AsyncTaskStatus,
  OperazioneAsincronaWrapper,
} from '../../models/async';
import {
  AsnycTaskWatcher,
  AsyncTaskService,
} from '../../services/async-task.service';

export interface TaskModalOptions {
  taskUUID: string;
  showMessages?: boolean;
  layout?: 'default' | 'minimal';
  maxTaskDepth?: number;
}

@Component({
  selector: 'app-task-modal',
  templateUrl: './task-modal.component.html',
  styles: ['./task-modal.component.scss']
})
export class TaskModalComponent implements OnInit {
  options?: TaskModalOptions;

  started = false;
  task: OperazioneAsincrona | null = null;
  finished = false;
  steps: OperazioneAsincrona[] | null = null;
  lastStepDescription?: string;

  icon: string;

  POSSIBLE_ICONS = [
    'fas fa-user-astronaut',
    'fas fa-meteor',
    'far fa-moon',
    'fas fa-robot',
    'fas fa-rocket',
    'fas fa-satellite',
    'fas fa-satellite-dish',
    'fas fa-globe-europe',
  ];

  watcher: AsnycTaskWatcher<unknown> | null = null;

  constructor(
    private logger: NGXLogger,
    private taskService: AsyncTaskService,
    public activeModal: NgbActiveModal
  ) {
    this.icon = this.POSSIBLE_ICONS[Math.floor(Math.random() * this.POSSIBLE_ICONS.length)];
  }

  get maxTaskDepth(): number {
    return this.options?.maxTaskDepth ?? 100;
  }

  get activeLayout(): 'default' | 'minimal' {
    return this.options?.layout ?? 'default';
  }

  get isDefaultLayout(): boolean {
    return this.activeLayout === 'default';
  }

  get isMinimalLayout(): boolean {
    return this.activeLayout === 'minimal';
  }

  get showMessages(): boolean {
    return this.options?.showMessages ?? true;
  }

  get id(): string {
    if (!this.options?.taskUUID) {
      throw new Error('No task UUID');
    }
    return this.options.taskUUID;
  }

  get autoCloseDelaySuccess(): number {
    if (this.isDefaultLayout) {
      return 2000;
    } else {
      return 100;
    }
  }

  get autoCloseDelayFailure(): number {
    if (this.isDefaultLayout) {
      return 2500;
    } else {
      return 100;
    }
  }

  ngOnInit(): void {
    // NOP
  }

  cancel() {
    // this.activeModal.dismiss('cancel');
    this.logger.info('cancel signal received');
  }

  public initialize(options: TaskModalOptions) {
    this.options = options;
    this.lastStepDescription = 'Inizializzazione';

    this.watcher = this.taskService.watcher(options.taskUUID);

    this.watcher.started.subscribe(() => this.started = true);
    this.watcher.finalized.subscribe(() => this.finished = true);
    this.watcher.updated.subscribe(raw => {
      this.task = this.parse(raw);
      this.steps = this.task.steps ?? [];
      this.computeLastStepDescription();
    });

    this.watcher.completed.subscribe(() => {
      setTimeout(() => this.activeModal.close(this.task), this.autoCloseDelaySuccess);
    });

    this.watcher.failed.subscribe(() => {
      setTimeout(() => this.activeModal.dismiss(this.task), this.autoCloseDelayFailure);
    });

    this.watcher.start();
  }


  hasPendingChild(task: OperazioneAsincrona): boolean {
    if (task.steps && task.steps.length) {
      for (const step of task.steps) {
        if (step.stato === AsyncTaskStatus.STARTED) {
          return true;
        }
        if (this.hasPendingChild(step)) {
          return true;
        }
      }
    }
    return false;
  }

  enumLevel(num: number): number[] {
    const out = [];
    for (let i = 0; i < num; i ++) {
      out.push(i);
    }
    return out;
  }

  parse(raw: OperazioneAsincronaWrapper<any>, level = 0): OperazioneAsincronaWrapped {
    return {
      ...raw,
      level,
      steps: ( raw.steps ?? []).map(o => this.parse(o, level + 1))
    };
  }

  computeLastStepDescription(): void {
    if (this.finished || !this.steps?.length) {
      return;
    } else {
      this.lastStepDescription = this.getLastStep(this.steps)?.nome;
    }
  }

  private getLastStep(steps: OperazioneAsincrona[]): OperazioneAsincrona | null {
    if (!steps?.length) {
      return null;
    }
    const last = steps[steps.length - 1];
    if (last.steps?.length) {
      return this.getLastStep(last.steps);
    } else {
      return last;
    }
  }
}

interface OperazioneAsincronaWrapped extends OperazioneAsincronaWrapper<any> {
  level: number;
  steps: OperazioneAsincronaWrapped[];
}
