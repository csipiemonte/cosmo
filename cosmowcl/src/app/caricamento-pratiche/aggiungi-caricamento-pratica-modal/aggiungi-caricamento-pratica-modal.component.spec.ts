/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AggiungiCaricamentoPraticaModalComponent } from './aggiungi-caricamento-pratica-modal.component';

describe('AggiungiCaricamentoPraticaModalComponent', () => {
  let component: AggiungiCaricamentoPraticaModalComponent;
  let fixture: ComponentFixture<AggiungiCaricamentoPraticaModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AggiungiCaricamentoPraticaModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AggiungiCaricamentoPraticaModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
