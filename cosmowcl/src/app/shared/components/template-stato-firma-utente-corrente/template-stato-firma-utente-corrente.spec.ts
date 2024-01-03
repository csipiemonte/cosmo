/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateStatoFirmaUtenteCorrente } from './template-stato-firma-utente-corrente.component';

describe('TemplateStatoFirmaUtenteCorrente', () => {
  let component: TemplateStatoFirmaUtenteCorrente;
  let fixture: ComponentFixture<TemplateStatoFirmaUtenteCorrente>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TemplateStatoFirmaUtenteCorrente ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TemplateStatoFirmaUtenteCorrente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
