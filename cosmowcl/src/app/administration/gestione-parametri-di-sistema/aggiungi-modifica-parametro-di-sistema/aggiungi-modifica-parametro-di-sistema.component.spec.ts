/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AggiungiModificaParametroDiSistemaComponent } from './aggiungi-modifica-parametro-di-sistema.component';

describe('AggiungiModificaParametroDiSistemaComponent', () => {
  let component: AggiungiModificaParametroDiSistemaComponent;
  let fixture: ComponentFixture<AggiungiModificaParametroDiSistemaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AggiungiModificaParametroDiSistemaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AggiungiModificaParametroDiSistemaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
