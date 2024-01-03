/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaricamentiInBozzaModalComponent } from './caricamenti-in-bozza-modal.component';

describe('CaricamentiInBozzaModalComponent', () => {
  let component: CaricamentiInBozzaModalComponent;
  let fixture: ComponentFixture<CaricamentiInBozzaModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CaricamentiInBozzaModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CaricamentiInBozzaModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
