/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateStatoFeaDocumentiComponent } from './template-stato-fea-documenti.component';

describe('TemplateStatoFeaDocumentiComponent', () => {
  let component: TemplateStatoFeaDocumentiComponent;
  let fixture: ComponentFixture<TemplateStatoFeaDocumentiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TemplateStatoFeaDocumentiComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TemplateStatoFeaDocumentiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
