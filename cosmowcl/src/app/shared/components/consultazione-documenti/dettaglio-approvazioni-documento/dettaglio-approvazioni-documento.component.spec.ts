/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioApprovazioniDocumentoComponent } from './dettaglio-approvazioni-documento.component';

describe('DettaglioApprovazioniDocumentoComponent', () => {
  let component: DettaglioApprovazioniDocumentoComponent;
  let fixture: ComponentFixture<DettaglioApprovazioniDocumentoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DettaglioApprovazioniDocumentoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioApprovazioniDocumentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
