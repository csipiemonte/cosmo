/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestioneParametriDiSistemaComponent } from './gestione-parametri-di-sistema.component';

describe('GestioneParametriDiSistemaComponent', () => {
  let component: GestioneParametriDiSistemaComponent;
  let fixture: ComponentFixture<GestioneParametriDiSistemaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GestioneParametriDiSistemaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GestioneParametriDiSistemaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
