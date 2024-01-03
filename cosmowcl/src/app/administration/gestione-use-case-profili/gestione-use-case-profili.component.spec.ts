/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestioneUseCaseProfiliComponent } from './gestione-use-case-profili.component';

describe('GestioneUseCaseProfiliComponent', () => {
  let component: GestioneUseCaseProfiliComponent;
  let fixture: ComponentFixture<GestioneUseCaseProfiliComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GestioneUseCaseProfiliComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GestioneUseCaseProfiliComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
