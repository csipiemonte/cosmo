/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DueOpzioniComponent } from './due-opzioni.component';

describe('DueOpzioniComponent', () => {
  let component: DueOpzioniComponent;
  let fixture: ComponentFixture<DueOpzioniComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DueOpzioniComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DueOpzioniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
