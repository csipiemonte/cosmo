/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AggiungiPraticheDocumentiModalComponent } from './aggiungi-pratiche-documenti-modal.component';

describe('AggiungiPraticheDocumentiModalComponent', () => {
  let component: AggiungiPraticheDocumentiModalComponent;
  let fixture: ComponentFixture<AggiungiPraticheDocumentiModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AggiungiPraticheDocumentiModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AggiungiPraticheDocumentiModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
