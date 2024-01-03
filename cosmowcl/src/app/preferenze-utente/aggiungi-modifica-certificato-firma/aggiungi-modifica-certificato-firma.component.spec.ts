/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AggiungiModificaCertificatoFirmaComponent } from './aggiungi-modifica-certificato-firma.component';

describe('AggiungiModificaCertificatoFirmaComponent', () => {
  let component: AggiungiModificaCertificatoFirmaComponent;
  let fixture: ComponentFixture<AggiungiModificaCertificatoFirmaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AggiungiModificaCertificatoFirmaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AggiungiModificaCertificatoFirmaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
