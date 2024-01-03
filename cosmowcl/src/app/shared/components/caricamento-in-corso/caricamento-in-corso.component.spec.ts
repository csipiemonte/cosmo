/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CaricamentoInCorsoComponent } from './caricamento-in-corso.component';

describe('CaricamentoInCorsoComponent', () => {
  let component: CaricamentoInCorsoComponent;
  let fixture: ComponentFixture<CaricamentoInCorsoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CaricamentoInCorsoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CaricamentoInCorsoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
