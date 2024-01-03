/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskAutomaticiInErroreComponent } from './task-automatici-in-errore.component';

describe('TaskAutomaticiInErroreComponent', () => {
  let component: TaskAutomaticiInErroreComponent;
  let fixture: ComponentFixture<TaskAutomaticiInErroreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TaskAutomaticiInErroreComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskAutomaticiInErroreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
