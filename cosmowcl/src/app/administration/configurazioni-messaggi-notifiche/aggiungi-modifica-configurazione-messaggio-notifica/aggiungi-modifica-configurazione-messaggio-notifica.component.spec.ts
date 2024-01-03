/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AggiungiModificaConfigurazioneMessaggioNotificaComponent } from './aggiungi-modifica-configurazione-messaggio-notifica.component';

describe('AggiungiModificaConfigurazioneMessaggioNotificaComponent', () => {
  let component: AggiungiModificaConfigurazioneMessaggioNotificaComponent;
  let fixture: ComponentFixture<AggiungiModificaConfigurazioneMessaggioNotificaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AggiungiModificaConfigurazioneMessaggioNotificaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AggiungiModificaConfigurazioneMessaggioNotificaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
