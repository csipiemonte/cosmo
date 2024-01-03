/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioInformazioniSigilloElettronicoComponent } from './dettaglio-informazioni-sigillo-elettronico.component';

describe('DettaglioInformazioniSigilloElettronicoComponent', () => {
  let component: DettaglioInformazioniSigilloElettronicoComponent;
  let fixture: ComponentFixture<DettaglioInformazioniSigilloElettronicoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DettaglioInformazioniSigilloElettronicoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioInformazioniSigilloElettronicoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
