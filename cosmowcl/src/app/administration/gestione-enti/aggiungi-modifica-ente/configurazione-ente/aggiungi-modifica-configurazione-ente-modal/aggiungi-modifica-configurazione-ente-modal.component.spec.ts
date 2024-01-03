/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AggiungiModificaConfigurazioneEnteModalComponent } from './aggiungi-modifica-configurazione-ente-modal.component';

describe('AggiungiModificaConfigurazioneEnteModalComponent', () => {
  let component: AggiungiModificaConfigurazioneEnteModalComponent;
  let fixture: ComponentFixture<AggiungiModificaConfigurazioneEnteModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AggiungiModificaConfigurazioneEnteModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AggiungiModificaConfigurazioneEnteModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
