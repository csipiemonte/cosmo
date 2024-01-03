/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AggiungiModificaAppEsterneComponent } from './aggiungi-modifica-app-esterne.component';

describe('AggiungiModificaAppEsterneComponent', () => {
  let component: AggiungiModificaAppEsterneComponent;
  let fixture: ComponentFixture<AggiungiModificaAppEsterneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AggiungiModificaAppEsterneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AggiungiModificaAppEsterneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
