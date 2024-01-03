/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CaricamentoFallitoComponent } from './caricamento-fallito.component';

describe('CaricamentoFallitoComponent', () => {
  let component: CaricamentoFallitoComponent;
  let fixture: ComponentFixture<CaricamentoFallitoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CaricamentoFallitoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CaricamentoFallitoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
