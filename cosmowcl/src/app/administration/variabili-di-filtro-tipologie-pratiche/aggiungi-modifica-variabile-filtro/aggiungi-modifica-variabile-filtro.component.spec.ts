/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AggiungiModificaVariabileFiltroComponent } from './aggiungi-modifica-variabile-filtro.component';

describe('AggiungiModificaVariabileFiltroComponent', () => {
  let component: AggiungiModificaVariabileFiltroComponent;
  let fixture: ComponentFixture<AggiungiModificaVariabileFiltroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AggiungiModificaVariabileFiltroComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AggiungiModificaVariabileFiltroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
