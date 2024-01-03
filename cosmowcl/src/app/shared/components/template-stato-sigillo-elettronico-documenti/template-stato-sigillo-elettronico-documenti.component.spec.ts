/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateStatoSigilloElettronicoDocumentiComponent } from './template-stato-sigillo-elettronico-documenti.component';

describe('TemplateStatoSigilloElettronicoDocumentiComponent', () => {
  let component: TemplateStatoSigilloElettronicoDocumentiComponent;
  let fixture: ComponentFixture<TemplateStatoSigilloElettronicoDocumentiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TemplateStatoSigilloElettronicoDocumentiComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TemplateStatoSigilloElettronicoDocumentiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
