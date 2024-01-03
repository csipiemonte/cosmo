/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PraticheComponent } from './pratiche.component';

describe('PraticheComponent', () => {
  let component: PraticheComponent;
  let fixture: ComponentFixture<PraticheComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PraticheComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PraticheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
