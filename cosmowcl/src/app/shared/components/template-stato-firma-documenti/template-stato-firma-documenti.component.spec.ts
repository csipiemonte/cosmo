/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateStatoFirmaDocumentiComponent } from './template-stato-firma-documenti.component';

describe('TemplateStatoFirmaDocumentiComponent', () => {
  let component: TemplateStatoFirmaDocumentiComponent;
  let fixture: ComponentFixture<TemplateStatoFirmaDocumentiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TemplateStatoFirmaDocumentiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TemplateStatoFirmaDocumentiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
