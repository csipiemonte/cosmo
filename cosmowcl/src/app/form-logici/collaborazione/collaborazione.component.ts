/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Injector, OnInit, ViewChild } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { distinctUntilChanged, finalize, map, mergeMap, switchMap, tap } from 'rxjs/operators';
import { Task } from 'src/app/shared/models/api/cosmopratiche/task';
import { SecurityService } from 'src/app/shared/services/security.service';
import { TaskService } from 'src/app/shared/services/task.service';
import { UtentiService } from 'src/app/shared/services/utenti.service';
import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import { forkJoin } from 'rxjs';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { Commento } from 'src/app/shared/models/api/cosmobusiness/commento';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { Utente } from 'src/app/shared/models/api/cosmoauthorization/utente';

@Component({
  selector: 'app-collaborazione',
  templateUrl: './collaborazione.component.html',
  styleUrls: ['./collaborazione.component.scss']
})
export class CollaborazioneComponent extends FunzionalitaParentComponent implements OnInit {

  constructor(
    private utentiService: UtentiService,
    public injector: Injector,
    private securityService: SecurityService,
    private taskService: TaskService,
    private modalService: ModalService,
  ) {
    super(injector);
  }

  loading = 0;
  loadingError: any | null = null;

  userInfo: UserInfoWrapper | undefined;

  commenti: Commento[] = [];
  subtasks: Task[] | undefined;
  presentiTaskAttivi = false;

  // scenario 1
  selectedUser: Utente | undefined;
  commentText: string | undefined;

  // scenario 3
  ultimoSubtask: Task | undefined;
  assegnatarioUltimoSubtask: Utente | null = null;

  // scenario 4
  replyText: string | undefined;

  @ViewChild('scenario1form') scenario1form: any | undefined;
  @ViewChild('scenario3form') scenario3form: any | undefined;
  @ViewChild('scenario4form') scenario4form: any | undefined;

  formatter = (result: Utente) => result.cognome + ' ' + result.nome;

  public isValid(): boolean {
    if (this.readOnly) {
      return true;
    }
    if (this.scenario === 4) {
      if (this.commenti?.length && this.commenti[this.commenti.length - 1].cfAutore === this.userInfo?.codiceFiscale) {
        return true;
      }
      return false;
    }
    return true;
  }

  public isWip(): boolean {
    if (this.readOnly) {
      return false;
    }
    if (this.scenario === 4) {
      // get last comment
      if (this.commenti?.length && this.commenti[this.commenti.length - 1].cfAutore === this.userInfo?.codiceFiscale) {
        return false;
      }
      return true;
    }
    return false;
  }

  public update() {
    this.sendData({});
  }

  ngOnInit(): void {
    this.securityService.getCurrentUser().subscribe((userInfo) => {
      this.userInfo = userInfo;
      this.refresh().subscribe();
    });
  }

  get abilitaInvioCommento(): boolean {
    if (this.isCollaboratore) { return false; }
    if (this.loading) { return false; }
    if (this.loadingError) { return false; }
    if (this.scenario !== 1) { return false; }
    if (!this.selectedUser?.codiceFiscale) { return false; }
    if (!this.commentText) { return false; }
    if (!this.scenario1form?.valid) { return false; }
    return true;
  }

  get isCollaboratore(): boolean {
    return !!this.childTask;
  }

  get scenario(): number | undefined {
    if (this.loading || this.loadingError) {
      return;
    }

    if (this.isCollaboratore) {
      if (this.presentiTaskAttivi) {
        return 4;
      } else {
        return 5;
      }
    } else {
      if (this.presentiTaskAttivi) {
        return 2;
      } else {
        return 1;
      }
    }
  }

  hoverAssegna() {
    if (this.scenario1form) {
      Object.keys(this.scenario1form.controls).forEach(key => {
        this.scenario1form.controls[key].markAsTouched();
      });
    }
  }

  displayErrors(ctrl: any): boolean {
    return ctrl && ctrl.invalid && (ctrl.dirty || ctrl.touched || this.scenario1form?.touched);
  }

  refresh(): Observable<any> {
    if (!this.task.id) {
      return throwError('No task ID');
    }

    // controlla l'esistenza di subtask attivi
    this.loading++;
    this.loadingError = null;

    if (this.isCollaboratore) {
      return this.taskService.getCommentiTask(this.task.id)
      .pipe(
        tap(commenti => {
          this.subtasks = [];
          this.presentiTaskAttivi = true;
          this.commenti = commenti;
        }, failure => {
          this.loadingError = failure;
          return failure;
        }),
        finalize(() => {
          this.loading--;
          this.update();
        })
      );
    } else {

      return forkJoin({
        subtasks: this.taskService.getSubtasks(this.task.id),
        commenti: this.taskService.getCommentiTask(this.task.id),
      })
      .pipe(
        map(result => {
          return {
            ...result,
            ultimoSubtask: result.subtasks.slice().reverse().find(t => true)
          };
        }),
        mergeMap(result => {
          return (
            result.ultimoSubtask?.assignee?.length ?
              this.utentiService.getUtenteByCodiceFiscale(result.ultimoSubtask.assignee, Utils.require(this.userInfo?.ente?.id))
                : of(null)
          ).pipe(
            map(utente => {
              return {
                ...result,
                assegnatarioUltimoSubtask: utente
              };
            })
          );
        }),
        tap(result => {
          this.commenti = result.commenti;
          this.subtasks = result.subtasks;
          this.presentiTaskAttivi = !!(result.subtasks.find(t => !t.cancellationDate));
          this.ultimoSubtask = result.ultimoSubtask;
          this.assegnatarioUltimoSubtask = result.assegnatarioUltimoSubtask ?? null;
          if (this.assegnatarioUltimoSubtask) {
            this.selectedUser = this.assegnatarioUltimoSubtask;
          }
        }, failure => {
          this.loadingError = failure;
          return failure;
        }),
        finalize(() => {
          this.loading--;
          this.update();
        })
      );
    }
  }

  search = (text$: Observable<string>) => {
    return text$.pipe(
      distinctUntilChanged(),
      switchMap((searchText) => searchText.length < 3 ? [] : this.utentiService.getUtenti(JSON.stringify(this.getFilter(searchText)))
        .pipe(
          map(term =>
            term && term.utenti && term.utenti.length > 0 ? term.utenti : []
          )
        )
      )
    );
  }

  private getFilter(value: string) {
    if (!this.userInfo) {
      throw new Error('no user info');
    }

    const filter = {
      filter: {
        idEnte: { eq: this.userInfo.ente?.id },
        fullText: { ci: value },
        codiceFiscale: { nin: [this.userInfo.codiceFiscale] }
      }
    };
    return filter;
  }

  assegnaSubtask() {
    if (!this.commentText || !this.selectedUser) {
      return;
    }
    if (!this.task.id) {
      return throwError('No task ID');
    }

    // this.loading ++;
    const payload: Task = {
      // ...this.task,
      processInstanceId: this.task.processInstanceId,

      assignee: this.selectedUser.codiceFiscale,
      parentTaskId: this.task.id + '',
      name: 'Collaborazione a ' + this.task.name,
      description: 'Richiesta di collaborazione da ' + this.userInfo?.nome + ' ' + this.userInfo?.cognome + ' sul task ' + this.task.name,
      formKey: this.task.formKey,
      category: 'subtask'
    };

    forkJoin({
      commento: this.taskService.creaCommentoTask(this.task.id, this.commentText),
      subtask: this.taskService.creaSubtask(payload)
    }).pipe(
      finalize(() => {
        this.update();
        // this.loading --;
      })
    ).subscribe(() => {
      this.modalService.simpleInfo('form_logici.collaborazione_richiesta_correttamente');
      this.selectedUser = undefined;
      this.commentText = undefined;
      this.refresh().subscribe(() => {
        this.presentiTaskAttivi = true;
      });

    }, failure => {
      this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
    });
  }

  get abilitaInvioRispostaCollaboratore(): boolean {
    if (!this.isCollaboratore) { return false; }
    if (this.loading) { return false; }
    if (this.loadingError) { return false; }
    if (this.scenario !== 4) { return false; }
    if (!this.replyText) { return false; }
    if (!this.scenario4form?.valid) { return false; }
    return true;
  }

  inviaRispostaCollaboratore() {
    if (!this.replyText || !this.childTask) {
      return;
    }
    if (!this.task.id) {
      return throwError('No task ID');
    }

    // this.loading ++;

    forkJoin({
      // chiusura: this.taskService.chiudiTask(this.childTask),
      commento: this.taskService.creaCommentoTask(this.task.id, this.replyText, true),
    }).pipe(
      finalize(() => {
        this.update();
        // this.loading --;
      })
    ).subscribe(() => {
      this.modalService.simpleInfo('form_logici.collaborazione_risposta_correttamente');
      this.replyText = undefined;

      this.refresh().subscribe(() => {
      //  this.presentiTaskAttivi = false;
      });

    }, failure => {
      this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
    });
  }

}
