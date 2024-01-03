/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioVerificaFirmeComponent } from './dettaglio-verifica-firme.component';

describe('DettaglioVerificaFirmeComponent', () => {
  let component: DettaglioVerificaFirmeComponent;
  let fixture: ComponentFixture<DettaglioVerificaFirmeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioVerificaFirmeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioVerificaFirmeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
