/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VariabiliDiFiltroTipologiePraticheComponent } from './variabili-di-filtro-tipologie-pratiche.component';

describe('VariabiliDiFiltroTipologiePraticheComponent', () => {
  let component: VariabiliDiFiltroTipologiePraticheComponent;
  let fixture: ComponentFixture<VariabiliDiFiltroTipologiePraticheComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VariabiliDiFiltroTipologiePraticheComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VariabiliDiFiltroTipologiePraticheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
