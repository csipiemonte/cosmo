/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DatiPraticaComponent } from './dati-pratica.component';

describe('DatiPraticaComponent', () => {
  let component: DatiPraticaComponent;
  let fixture: ComponentFixture<DatiPraticaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DatiPraticaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiPraticaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
