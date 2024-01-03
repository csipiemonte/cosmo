/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreazionePraticaComponent } from './creazione-pratica.component';

describe('CreazionePraticaComponent', () => {
  let component: CreazionePraticaComponent;
  let fixture: ComponentFixture<CreazionePraticaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreazionePraticaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreazionePraticaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
