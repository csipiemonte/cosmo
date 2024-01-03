/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AggiungiModificaIstanzaComponent } from './aggiungi-modifica-istanza.component';

describe('AggiungiModificaIstanzaComponent', () => {
  let component: AggiungiModificaIstanzaComponent;
  let fixture: ComponentFixture<AggiungiModificaIstanzaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AggiungiModificaIstanzaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AggiungiModificaIstanzaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
