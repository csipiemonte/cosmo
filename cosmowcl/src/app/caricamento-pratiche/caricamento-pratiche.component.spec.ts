/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaricamentoPraticheComponent } from './caricamento-pratiche.component';

describe('CaricamentoPraticheComponent', () => {
  let component: CaricamentoPraticheComponent;
  let fixture: ComponentFixture<CaricamentoPraticheComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CaricamentoPraticheComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CaricamentoPraticheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
