/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AggiungiModificaUseCaseProfiliComponent } from './aggiungi-modifica-use-case-profili.component';

describe('AggiungiModificaUseCaseProfiliComponent', () => {
  let component: AggiungiModificaUseCaseProfiliComponent;
  let fixture: ComponentFixture<AggiungiModificaUseCaseProfiliComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AggiungiModificaUseCaseProfiliComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AggiungiModificaUseCaseProfiliComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
