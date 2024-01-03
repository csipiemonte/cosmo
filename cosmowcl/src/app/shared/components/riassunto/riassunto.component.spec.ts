/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RiassuntoComponent } from './riassunto.component';

describe('RiassuntoComponent', () => {
  let component: RiassuntoComponent;
  let fixture: ComponentFixture<RiassuntoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RiassuntoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RiassuntoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
