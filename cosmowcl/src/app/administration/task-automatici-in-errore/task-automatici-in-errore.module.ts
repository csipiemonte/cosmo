/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskAutomaticiInErroreComponent } from './task-automatici-in-errore.component';
import { RouterModule, Routes } from '@angular/router';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';
import { TaskAutomaticiInErroreService } from 'src/app/shared/services/task-automatici-in-errore.service';
import { SharedModule } from 'src/app/shared/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxJsonViewerModule } from 'ngx-json-viewer';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';


const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    component: TaskAutomaticiInErroreComponent
  },
];

@NgModule({
  declarations: [TaskAutomaticiInErroreComponent],
  imports: [
    CommonModule,
    SharedModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
    NgxJsonViewerModule,
    DragDropModule
  ],
  providers:[
    TaskAutomaticiInErroreService
  ],
  exports:[
    RouterModule
  ]
})
export class TaskAutomaticiInErroreModule { }
